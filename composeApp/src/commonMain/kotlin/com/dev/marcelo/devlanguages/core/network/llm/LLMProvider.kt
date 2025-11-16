package com.dev.marcelo.devlanguages.core.network.llm

import com.dev.marcelo.devlanguages.core.network.ApiResult
import kotlinx.serialization.Serializable

/**
 * LLM Provider Interface
 * Interface abstrata para trocar facilmente entre diferentes LLMs
 * (Gemini, OpenAI, Claude, Cohere, etc)
 */
interface LLMProvider {
    /**
     * Gera jogos baseado no prompt do usuário
     *
     * @param request Requisição com prompt e parâmetros
     * @return Result com a resposta da LLM (explicação + jogos)
     */
    suspend fun generateGames(request: GameGenerationRequest): ApiResult<GameGenerationResponse>

    /**
     * Gera feedback para uma resposta do usuário
     *
     * @param userAnswer Resposta do usuário
     * @param correctAnswer Resposta correta
     * @param context Contexto adicional (opcional)
     * @return Result com feedback gerado
     */
    suspend fun generateFeedback(
        userAnswer: String,
        correctAnswer: String,
        context: String? = null
    ): ApiResult<FeedbackResponse>
}

/**
 * Requisição para geração de jogos
 */
@Serializable
data class GameGenerationRequest(
    val prompt: String,
    val language: String,
    val difficulty: String = "intermediate", // beginner, intermediate, advanced
    val numberOfGames: Int = 10
)

/**
 * Resposta da LLM com jogos gerados
 */
@Serializable
data class GameGenerationResponse(
    val topic: String,
    val explanation: String,
    val difficulty: String,
    val language: String,
    val games: List<GameData>
)

/**
 * Dados de um jogo individual
 * Formato genérico que suporta todos os tipos de jogos
 */
@Serializable
data class GameData(
    val id: String,
    val type: GameType,
    val question: String,
    val answer: String? = null,
    val options: List<String>? = null, // Para multiple choice ou fill blanks
    val pairs: Map<String, String>? = null, // Para matching
    val explanation: String
)

/**
 * Tipos de jogos suportados
 */
@Serializable
enum class GameType {
    MATCHING,
    TRANSLATION,
    FILL_BLANKS,
    MULTIPLE_CHOICE, // Futuro
    TRUE_FALSE // Futuro
}

/**
 * Resposta de feedback
 */
@Serializable
data class FeedbackResponse(
    val isCorrect: Boolean,
    val feedback: String,
    val explanation: String? = null
)

/**
 * Configuração do LLM Provider
 */
data class LLMConfig(
    val apiKey: String,
    val model: String = "default",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 2000
)
