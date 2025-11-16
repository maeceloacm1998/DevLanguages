package com.dev.marcelo.devlanguages.core.network.toon

import com.dev.marcelo.devlanguages.core.network.llm.GameData
import com.dev.marcelo.devlanguages.core.network.llm.GameGenerationResponse
import com.dev.marcelo.devlanguages.core.network.llm.GameType
import io.github.aakira.napier.Napier

/**
 * TOON Parser
 * Parser manual para Token-Oriented Object Notation
 *
 * Formato TOON reduz tokens em ~40% comparado ao JSON
 * Exemplo:
 * ```
 * topic: Past Tense
 * explanation: The past tense...
 * games[3]{type,question,answer,options,explanation}:
 *  translation,"I walked","Eu caminhei",null,"explanation"
 *  fill_blanks,"She ___ yesterday",went,"[went,goes]","explanation"
 * ```
 */
object ToonParser {

    /**
     * Parse resposta TOON da LLM para GameGenerationResponse
     */
    fun parseGameGenerationResponse(toonText: String): GameGenerationResponse? {
        try {
            val lines = toonText.trim().lines()
            val metadata = mutableMapOf<String, String>()
            val games = mutableListOf<GameData>()

            var inGamesSection = false
            var gameFields: List<String> = emptyList()

            for (line in lines) {
                val trimmedLine = line.trim()

                if (trimmedLine.isEmpty()) continue

                when {
                    // Parse metadata (key: value)
                    !inGamesSection && trimmedLine.contains(":") && !trimmedLine.startsWith("games") -> {
                        val (key, value) = trimmedLine.split(":", limit = 2)
                        metadata[key.trim()] = value.trim()
                    }

                    // Games array header: games[N]{fields}:
                    trimmedLine.startsWith("games[") -> {
                        inGamesSection = true
                        // Extract fields from games[3]{type,question,answer,options,explanation}:
                        val fieldsMatch = Regex("\\{(.+?)\\}").find(trimmedLine)
                        gameFields = fieldsMatch?.groupValues?.get(1)?.split(",")?.map { it.trim() } ?: emptyList()
                    }

                    // Game data rows (indented)
                    inGamesSection && trimmedLine.isNotEmpty() -> {
                        val gameData = parseGameDataRow(trimmedLine, gameFields)
                        if (gameData != null) {
                            games.add(gameData)
                        }
                    }
                }
            }

            return GameGenerationResponse(
                topic = metadata["topic"] ?: "",
                explanation = metadata["explanation"] ?: "",
                difficulty = metadata["difficulty"] ?: "intermediate",
                language = metadata["language"] ?: "english",
                games = games
            )
        } catch (e: Exception) {
            Napier.e("Error parsing TOON: ${e.message}", e)
            return null
        }
    }

    /**
     * Parse uma linha de dados de jogo
     * Formato: type,"question","answer","[opt1,opt2]","explanation"
     */
    private fun parseGameDataRow(line: String, fields: List<String>): GameData? {
        try {
            val values = parseCSVLine(line)
            if (values.size != fields.size) {
                Napier.w("Field count mismatch: expected ${fields.size}, got ${values.size}")
                return null
            }

            val dataMap = fields.zip(values).toMap()

            return GameData(
                id = generateId(),
                type = parseGameType(dataMap["type"] ?: ""),
                question = dataMap["question"]?.unquote() ?: "",
                answer = dataMap["answer"]?.unquote()?.takeIf { it != "null" },
                options = parseOptions(dataMap["options"]?.unquote()),
                pairs = parsePairs(dataMap["pairs"]?.unquote()),
                explanation = dataMap["explanation"]?.unquote() ?: ""
            )
        } catch (e: Exception) {
            Napier.e("Error parsing game row: ${e.message}", e)
            return null
        }
    }

    /**
     * Parse CSV line respeitando aspas
     */
    private fun parseCSVLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false

        for (char in line) {
            when {
                char == '"' -> inQuotes = !inQuotes
                char == ',' && !inQuotes -> {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                }
                else -> current.append(char)
            }
        }
        result.add(current.toString().trim())
        return result
    }

    /**
     * Parse tipo de jogo
     */
    private fun parseGameType(typeStr: String): GameType {
        return when (typeStr.lowercase()) {
            "translation" -> GameType.TRANSLATION
            "fill_blanks", "fill_in_blanks" -> GameType.FILL_BLANKS
            "matching" -> GameType.MATCHING
            "multiple_choice" -> GameType.MULTIPLE_CHOICE
            "true_false" -> GameType.TRUE_FALSE
            else -> GameType.TRANSLATION
        }
    }

    /**
     * Parse opções: "[opt1,opt2,opt3]" -> List<String>
     */
    private fun parseOptions(optionsStr: String?): List<String>? {
        if (optionsStr.isNullOrBlank() || optionsStr == "null") return null

        val cleaned = optionsStr.trim().removeSurrounding("[", "]")
        return cleaned.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    /**
     * Parse pares para matching: "word1|translation1,word2|translation2"
     */
    private fun parsePairs(pairsStr: String?): Map<String, String>? {
        if (pairsStr.isNullOrBlank() || pairsStr == "null") return null

        return pairsStr.split(",")
            .mapNotNull { pair ->
                val parts = pair.split("|")
                if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
            }
            .toMap()
            .takeIf { it.isNotEmpty() }
    }

    /**
     * Remove aspas de uma string
     */
    private fun String.unquote(): String {
        return this.trim().removeSurrounding("\"")
    }

    /**
     * Gera ID único para o jogo
     */
    private fun generateId(): String {
        return "game_${kotlin.random.Random.nextLong()}"
    }

    /**
     * Serializa GameGenerationResponse para formato TOON
     * (Útil para testes ou cache)
     */
    fun serializeToToon(response: GameGenerationResponse): String {
        val sb = StringBuilder()

        // Metadata
        sb.appendLine("topic: ${response.topic}")
        sb.appendLine("explanation: ${response.explanation}")
        sb.appendLine("difficulty: ${response.difficulty}")
        sb.appendLine("language: ${response.language}")
        sb.appendLine()

        // Games array header
        sb.appendLine("games[${response.games.size}]{type,question,answer,options,explanation}:")

        // Games data
        response.games.forEach { game ->
            val type = game.type.name.lowercase()
            val question = "\"${game.question}\""
            val answer = game.answer?.let { "\"$it\"" } ?: "null"
            val options = game.options?.let { "[${it.joinToString(",")}]" } ?: "null"
            val explanation = "\"${game.explanation}\""

            sb.appendLine(" $type,$question,$answer,$options,$explanation")
        }

        return sb.toString()
    }
}
