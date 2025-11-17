package com.dev.marcelo.devlanguages.features.auth.data.datasource

import com.dev.marcelo.devlanguages.features.auth.domain.model.User

/**
 * AuthDataSource Interface
 * Define o contrato para operações de autenticação na camada de dados
 */
interface AuthDataSource {
    /**
     * Login com email e senha
     */
    suspend fun signInWithEmail(email: String, password: String): Result<User>

    /**
     * Cadastro com email e senha
     */
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User>

    /**
     * Login com Google
     */
    suspend fun signInWithGoogle(): Result<User>

    /**
     * Login com Apple
     */
    suspend fun signInWithApple(): Result<User>

    /**
     * Login anônimo
     */
    suspend fun signInAnonymously(): Result<User>

    /**
     * Logout
     */
    suspend fun signOut(): Result<Unit>

    /**
     * Obtém o usuário atual
     */
    suspend fun getCurrentUser(): User?

    /**
     * Verifica se há usuário autenticado
     */
    suspend fun isUserAuthenticated(): Boolean
}
