package com.dev.marcelo.devlanguages.core.di

import com.dev.marcelo.devlanguages.core.network.HttpClientProvider
import com.dev.marcelo.devlanguages.core.network.llm.AppLLMConfig
import com.dev.marcelo.devlanguages.core.network.llm.GeminiProvider
import com.dev.marcelo.devlanguages.core.network.llm.LLMProvider
import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Network Koin Module
 * Módulo de injeção de dependência para configurações de rede
 */
val networkModule = module {
    // Ktor HttpClient
    single<HttpClient> { HttpClientProvider.client }

    // LLM Provider - Gemini
    single<LLMProvider> {
        GeminiProvider(
            httpClient = get(),
            config = AppLLMConfig.getGeminiConfig()
        )
    }
}
