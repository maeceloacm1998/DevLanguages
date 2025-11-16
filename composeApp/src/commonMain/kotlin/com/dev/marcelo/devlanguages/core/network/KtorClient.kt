package com.dev.marcelo.devlanguages.core.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Ktor HTTP Client Configuration
 * Cliente HTTP configurado para toda a aplicação
 */

/**
 * Cria e configura o HttpClient para uso na aplicação
 */
fun createHttpClient(): HttpClient {
    return HttpClient {
        // Content Negotiation - JSON serialization/deserialization
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Ignora campos desconhecidos no JSON
                encodeDefaults = true
                explicitNulls = false
            })
        }

        // Logging - Log de requisições HTTP
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "HTTP Client", message = message)
                }
            }
            level = LogLevel.HEADERS // ALL, HEADERS, BODY, INFO, NONE
        }

        // Default Request - Configurações padrão para todas as requisições
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        // Timeout configuration (pode ser ajustado conforme necessário)
        // Para LLM, pode precisar de timeout maior
        // expectSuccess = true // Lança exceção em status codes de erro
    }
}

/**
 * Instância singleton do HttpClient
 * Reusar a mesma instância é recomendado
 */
object HttpClientProvider {
    val client: HttpClient by lazy {
        createHttpClient()
    }

    /**
     * Fecha o client (geralmente ao finalizar o app)
     */
    fun close() {
        client.close()
    }
}
