package com.dev.marcelo.devlanguages.core.network.llm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gemini API Models (DTOs)
 * https://ai.google.dev/api/rest
 */

/**
 * Request para Gemini API
 */
@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
)

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String = "user"
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class GenerationConfig(
    val temperature: Float = 0.7f,
    val maxOutputTokens: Int = 2000,
    val topK: Int? = null,
    val topP: Float? = null
)

/**
 * Response da Gemini API
 */
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    val promptFeedback: PromptFeedback? = null
)

@Serializable
data class Candidate(
    val content: Content,
    val finishReason: String? = null,
    val safetyRatings: List<SafetyRating>? = null
)

@Serializable
data class PromptFeedback(
    val safetyRatings: List<SafetyRating>? = null,
    val blockReason: String? = null
)

@Serializable
data class SafetyRating(
    val category: String,
    val probability: String
)

/**
 * Extension para obter o texto da resposta
 */
fun GeminiResponse.getTextOrNull(): String? {
    return candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
}
