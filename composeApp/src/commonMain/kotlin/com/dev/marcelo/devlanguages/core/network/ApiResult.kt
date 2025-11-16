package com.dev.marcelo.devlanguages.core.network

/**
 * API Result Wrapper
 * Sealed class para encapsular resultados de chamadas de API
 *
 * Uso:
 * ```
 * when (result) {
 *     is ApiResult.Success -> handleSuccess(result.data)
 *     is ApiResult.Error -> handleError(result.exception)
 * }
 * ```
 */
sealed class ApiResult<out T> {
    /**
     * Resultado bem-sucedido
     * @param data Dados retornados pela API
     */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * Resultado com erro
     * @param exception Exceção que ocorreu
     * @param code Código de erro HTTP (opcional)
     */
    data class Error(
        val exception: NetworkException,
        val code: Int? = null
    ) : ApiResult<Nothing>()
}

/**
 * Extension: Mapeia o resultado de sucesso para outro tipo
 */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> ApiResult.Error(exception, code)
    }
}

/**
 * Extension: Executa ação se for sucesso
 */
inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action(data)
    }
    return this
}

/**
 * Extension: Executa ação se for erro
 */
inline fun <T> ApiResult<T>.onError(action: (NetworkException) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) {
        action(exception)
    }
    return this
}

/**
 * Extension: Retorna dados ou null
 */
fun <T> ApiResult<T>.getOrNull(): T? {
    return when (this) {
        is ApiResult.Success -> data
        is ApiResult.Error -> null
    }
}

/**
 * Extension: Retorna dados ou valor padrão
 */
fun <T> ApiResult<T>.getOrDefault(default: T): T {
    return when (this) {
        is ApiResult.Success -> data
        is ApiResult.Error -> default
    }
}

/**
 * Extension: Retorna dados ou executa bloco
 */
inline fun <T> ApiResult<T>.getOrElse(onError: (NetworkException) -> T): T {
    return when (this) {
        is ApiResult.Success -> data
        is ApiResult.Error -> onError(exception)
    }
}

/**
 * Extension: Verifica se é sucesso
 */
val <T> ApiResult<T>.isSuccess: Boolean
    get() = this is ApiResult.Success

/**
 * Extension: Verifica se é erro
 */
val <T> ApiResult<T>.isError: Boolean
    get() = this is ApiResult.Error
