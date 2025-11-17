package com.dev.marcelo.devlanguages.core.utils

/**
 * Result
 * Sealed class genérica para representar o resultado de uma operação
 *
 * @param T Tipo do dado em caso de sucesso
 */
sealed class Result<out T> {
    /**
     * Sucesso na operação
     * @param data Dados retornados
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Erro na operação
     * @param message Mensagem de erro
     * @param exception Exceção que causou o erro (opcional)
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : Result<Nothing>()

    /**
     * Verifica se o resultado é sucesso
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Verifica se o resultado é erro
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Retorna os dados se sucesso, null caso contrário
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    /**
     * Retorna a mensagem de erro se houver, null caso contrário
     */
    fun errorOrNull(): String? = when (this) {
        is Success -> null
        is Error -> message
    }
}
