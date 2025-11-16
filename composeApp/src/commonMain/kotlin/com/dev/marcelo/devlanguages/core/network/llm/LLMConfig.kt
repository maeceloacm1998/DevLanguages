package com.dev.marcelo.devlanguages.core.network.llm

/**
 * LLM Configuration
 *
 * ⚠️ IMPORTANTE - SEGURANÇA:
 * - A API key está temporariamente hardcoded aqui para desenvolvimento
 * - NUNCA commite este arquivo com a key real para produção
 * - Em produção, use variáveis de ambiente ou BuildConfig
 * - Este arquivo deve estar no .gitignore ou a key deve vir de .env
 *
 * TODO: Mover API key para:
 * - Android: BuildConfig ou local.properties
 * - iOS: Info.plist ou Config.xcconfig
 * - Ou usar arquivo .env
 */
object AppLLMConfig {
    /**
     * ⚠️ TEMPORÁRIO - API Key do Gemini
     * Mover para configuração segura em produção!
     */
    private const val GEMINI_API_KEY = "AIzaSyA9NGOUzgowj7pRVNIV1BOjc5NZA9xy_aE"

    /**
     * Configuração para Gemini Provider
     */
    fun getGeminiConfig(): LLMConfig {
        return LLMConfig(
            apiKey = GEMINI_API_KEY,
            model = "gemini-1.5-flash", // Modelo gratuito e rápido
            temperature = 0.7f,
            maxTokens = 2000
        )
    }

    /**
     * Configuração alternativa para respostas mais criativas
     */
    fun getGeminiConfigCreative(): LLMConfig {
        return LLMConfig(
            apiKey = GEMINI_API_KEY,
            model = "gemini-1.5-flash",
            temperature = 0.9f, // Mais criativo
            maxTokens = 2500
        )
    }

    /**
     * Configuração alternativa para respostas mais precisas/determinísticas
     */
    fun getGeminiConfigPrecise(): LLMConfig {
        return LLMConfig(
            apiKey = GEMINI_API_KEY,
            model = "gemini-1.5-flash",
            temperature = 0.3f, // Mais preciso
            maxTokens = 1500
        )
    }
}
