package com.dev.marcelo.devlanguages.core.network.llm

import com.dev.marcelo.devlanguages.core.network.ApiResult
import com.dev.marcelo.devlanguages.core.network.NetworkException
import com.dev.marcelo.devlanguages.core.network.toon.ToonParser
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Gemini Provider Implementation
 * Provider para Google Gemini AI
 *
 * Documentação: https://ai.google.dev/api/rest
 */
class GeminiProvider(
    private val httpClient: HttpClient,
    private val config: LLMConfig
) : LLMProvider {

    companion object {
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
        private const val DEFAULT_MODEL = "gemini-1.5-flash" // Modelo gratuito mais rápido
    }

    /**
     * Gera jogos baseado no prompt do usuário
     */
    override suspend fun generateGames(request: GameGenerationRequest): ApiResult<GameGenerationResponse> {
        return try {
            // Cria prompt instruindo a LLM a responder em formato TOON
            val prompt = buildGameGenerationPrompt(request)

            // Faz chamada para Gemini API
            val geminiRequest = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt))
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = config.temperature,
                    maxOutputTokens = config.maxTokens
                )
            )

            val response: HttpResponse = httpClient.post(
                urlString = "$BASE_URL/models/${config.model}:generateContent?key=${config.apiKey}"
            ) {
                contentType(ContentType.Application.Json)
                setBody(geminiRequest)
            }

            // Parse resposta
            val geminiResponse: GeminiResponse = response.body()
            val toonText = geminiResponse.getTextOrNull()

            if (toonText.isNullOrBlank()) {
                Napier.e("Gemini returned empty response")
                return ApiResult.Error(
                    NetworkException.ParseError("Resposta vazia da LLM")
                )
            }

            Napier.d("Gemini TOON Response:\n$toonText")

            // Parse TOON para objetos
            val gameResponse = ToonParser.parseGameGenerationResponse(toonText)

            if (gameResponse == null) {
                Napier.e("Failed to parse TOON response")
                return ApiResult.Error(
                    NetworkException.ParseError("Erro ao processar resposta da LLM")
                )
            }

            ApiResult.Success(gameResponse)

        } catch (e: Exception) {
            Napier.e("Error calling Gemini API", e)
            ApiResult.Error(
                NetworkException.Unknown(
                    message = e.message ?: "Erro ao gerar jogos",
                    cause = e
                )
            )
        }
    }

    /**
     * Gera feedback para resposta do usuário
     */
    override suspend fun generateFeedback(
        userAnswer: String,
        correctAnswer: String,
        context: String?
    ): ApiResult<FeedbackResponse> {
        return try {
            val prompt = buildFeedbackPrompt(userAnswer, correctAnswer, context)

            val geminiRequest = GeminiRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = prompt)))
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.3f, // Mais determinístico para feedback
                    maxOutputTokens = 500
                )
            )

            val response: HttpResponse = httpClient.post(
                urlString = "$BASE_URL/models/${config.model}:generateContent?key=${config.apiKey}"
            ) {
                contentType(ContentType.Application.Json)
                setBody(geminiRequest)
            }

            val geminiResponse: GeminiResponse = response.body()
            val feedbackText = geminiResponse.getTextOrNull() ?: ""

            // Parse simples do feedback (formato: CORRETO/INCORRETO|feedback)
            val parts = feedbackText.split("|", limit = 2)
            val isCorrect = parts.getOrNull(0)?.trim()?.equals("CORRETO", ignoreCase = true) == true
            val feedback = parts.getOrNull(1)?.trim() ?: feedbackText

            ApiResult.Success(
                FeedbackResponse(
                    isCorrect = isCorrect,
                    feedback = feedback,
                    explanation = null
                )
            )

        } catch (e: Exception) {
            Napier.e("Error generating feedback", e)
            ApiResult.Error(
                NetworkException.Unknown(
                    message = "Erro ao gerar feedback",
                    cause = e
                )
            )
        }
    }

    /**
     * Constrói prompt para geração de jogos em formato TOON
     */
    private fun buildGameGenerationPrompt(request: GameGenerationRequest): String {
        return """
Você é um assistente especializado em criar exercícios de idiomas.

INSTRUÇÃO: Crie ${request.numberOfGames} exercícios sobre "${request.prompt}" em ${request.language}.

IMPORTANTE: Responda APENAS no formato TOON (Token-Oriented Object Notation) abaixo. NÃO adicione texto extra.

FORMATO DA RESPOSTA:
topic: [título do tema]
explanation: [explicação educacional do tema em 2-3 frases]
difficulty: ${request.difficulty}
language: ${request.language}

games[${request.numberOfGames}]{type,question,answer,options,explanation}:
 [tipo],[pergunta],[resposta],[opções],[explicação]
 [tipo],[pergunta],[resposta],[opções],[explicação]
 ...

TIPOS DE JOGOS DISPONÍVEIS:
- translation: Tradução (answer = tradução correta, options = null)
- fill_blanks: Preencher lacuna (answer = resposta correta, options = [opt1,opt2,opt3,opt4])
- matching: Associação (pairs no formato "palavra1|tradução1,palavra2|tradução2", options e answer = null)

REGRAS:
1. Use aspas duplas em textos: "exemplo"
2. Use null para campos vazios
3. Para options: "[opção1,opção2,opção3]" ou null
4. Para matching: use pairs ao invés de question/answer
5. Cada jogo deve ter uma explicação educacional

EXEMPLO:
topic: Past Tense Verbs
explanation: The past tense is used to describe actions that have already happened.
difficulty: intermediate
language: english

games[3]{type,question,answer,options,explanation}:
 translation,"I walked to school","Eu caminhei para a escola",null,"'Walked' is the past tense of 'walk'"
 fill_blanks,"She ___ to the party yesterday",went,"[went,goes,go,gone]","Use 'went' for past tense of 'go'"
 matching,"run|ran,eat|ate,go|went",null,null,"Match irregular verbs with their past forms"

AGORA CRIE OS EXERCÍCIOS:
        """.trimIndent()
    }

    /**
     * Constrói prompt para feedback
     */
    private fun buildFeedbackPrompt(
        userAnswer: String,
        correctAnswer: String,
        context: String?
    ): String {
        return """
Compare a resposta do usuário com a resposta correta.

Resposta do usuário: "$userAnswer"
Resposta correta: "$correctAnswer"
${context?.let { "Contexto: $it" } ?: ""}

FORMATO DA RESPOSTA: CORRETO|feedback OU INCORRETO|feedback

Se estiver correto ou muito próximo: "CORRETO|Excelente! Você acertou."
Se estiver incorreto: "INCORRETO|[explicação do erro e dica para melhorar]"

Seja educativo e encorajador.
        """.trimIndent()
    }
}
