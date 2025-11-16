package com.dev.marcelo.devlanguages.core.network

/**
 * Network Exception
 * Exceções relacionadas a chamadas de rede
 */
sealed class NetworkException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Sem conexão com a internet
     */
    data class NoConnection(
        override val message: String = "Sem conexão com a internet"
    ) : NetworkException(message)

    /**
     * Timeout na requisição
     */
    data class Timeout(
        override val message: String = "Tempo de espera excedido"
    ) : NetworkException(message)

    /**
     * Erro de servidor (5xx)
     */
    data class ServerError(
        val code: Int,
        override val message: String = "Erro no servidor (código $code)"
    ) : NetworkException(message)

    /**
     * Erro do cliente (4xx)
     */
    data class ClientError(
        val code: Int,
        override val message: String = "Erro na requisição (código $code)"
    ) : NetworkException(message)

    /**
     * Erro ao fazer parse do JSON
     */
    data class ParseError(
        override val message: String = "Erro ao processar resposta",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)

    /**
     * Erro desconhecido
     */
    data class Unknown(
        override val message: String = "Erro desconhecido",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)

    /**
     * Requisição não autorizada (401)
     */
    data class Unauthorized(
        override val message: String = "Não autorizado. Faça login novamente."
    ) : NetworkException(message)

    /**
     * Recurso não encontrado (404)
     */
    data class NotFound(
        override val message: String = "Recurso não encontrado"
    ) : NetworkException(message)

    /**
     * Rate limit excedido (429)
     * Importante para LLM APIs com limites de uso
     */
    data class RateLimitExceeded(
        override val message: String = "Limite de uso excedido. Tente novamente mais tarde."
    ) : NetworkException(message)

    /**
     * API Key inválida
     */
    data class InvalidApiKey(
        override val message: String = "Chave de API inválida"
    ) : NetworkException(message)

    /**
     * Limite de uso diário atingido (Freemium)
     */
    data class DailyLimitReached(
        override val message: String = "Limite diário atingido. Faça upgrade para continuar."
    ) : NetworkException(message)
}

/**
 * Extension: Mensagem user-friendly para exibir na UI
 */
val NetworkException.userFriendlyMessage: String
    get() = when (this) {
        is NetworkException.NoConnection -> "Verifique sua conexão com a internet"
        is NetworkException.Timeout -> "A conexão está demorando muito. Tente novamente"
        is NetworkException.ServerError -> "Nossos servidores estão com problemas. Tente mais tarde"
        is NetworkException.ClientError -> "Algo deu errado. Tente novamente"
        is NetworkException.ParseError -> "Erro ao processar dados"
        is NetworkException.Unauthorized -> "Você precisa fazer login novamente"
        is NetworkException.NotFound -> "Conteúdo não encontrado"
        is NetworkException.RateLimitExceeded -> "Você atingiu o limite de uso. Aguarde alguns minutos"
        is NetworkException.InvalidApiKey -> "Configuração inválida. Entre em contato com o suporte"
        is NetworkException.DailyLimitReached -> "Você atingiu o limite diário! Faça upgrade para Premium"
        is NetworkException.Unknown -> "Algo deu errado. Tente novamente"
    }

/**
 * Extension: Verifica se o erro é recuperável (usuário pode tentar novamente)
 */
val NetworkException.isRetryable: Boolean
    get() = when (this) {
        is NetworkException.NoConnection -> true
        is NetworkException.Timeout -> true
        is NetworkException.ServerError -> true
        is NetworkException.RateLimitExceeded -> false // Precisa esperar
        is NetworkException.Unauthorized -> false // Precisa re-autenticar
        is NetworkException.DailyLimitReached -> false // Precisa upgrade
        else -> false
    }
