package com.dev.marcelo.devlanguages.core.di

import com.dev.marcelo.devlanguages.core.network.HttpClientProvider
import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Network Koin Module
 * Módulo de injeção de dependência para configurações de rede
 */
val networkModule = module {
    // Ktor HttpClient
    single<HttpClient> { HttpClientProvider.client }

    // LLM Provider será adicionado aqui quando implementar Gemini
    // single<LLMProvider> { GeminiProvider(get(), apiKey = ...) }
}
