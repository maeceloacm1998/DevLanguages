package com.dev.marcelo.devlanguages.features.auth.domain.model

/**
 * AuthResult
 * Sealed class que representa o resultado de uma operação de autenticação
 */
sealed class AuthResult {
    /**
     * Sucesso na autenticação
     */
    data class Success(val user: User) : AuthResult()

    /**
     * Erro na autenticação
     */
    data class Error(val message: String, val exception: Throwable? = null) : AuthResult()

    /**
     * Operação em andamento
     */
    data object Loading : AuthResult()
}
