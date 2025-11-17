package com.dev.marcelo.devlanguages.features.auth.domain.repository

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.model.User

/**
 * AuthRepository Interface
 * Define o contrato para operações de autenticação
 *
 * Esta interface segue o Dependency Inversion Principle,
 * permitindo que o Domain não dependa de implementações concretas
 */
interface AuthRepository {
    /**
     * Login com email e senha
     *
     * @param email Email do usuário
     * @param password Senha do usuário
     * @return AuthResult com sucesso ou erro
     */
    suspend fun signInWithEmail(email: String, password: String): AuthResult

    /**
     * Cadastro com email e senha
     *
     * @param email Email do usuário
     * @param password Senha do usuário
     * @param displayName Nome de exibição do usuário
     * @return AuthResult com sucesso ou erro
     */
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): AuthResult

    /**
     * Login com Google
     *
     * @return AuthResult com sucesso ou erro
     */
    suspend fun signInWithGoogle(): AuthResult

    /**
     * Login com Apple (iOS)
     *
     * @return AuthResult com sucesso ou erro
     */
    suspend fun signInWithApple(): AuthResult

    /**
     * Login anônimo
     *
     * @return AuthResult com sucesso ou erro
     */
    suspend fun signInAnonymously(): AuthResult

    /**
     * Logout
     *
     * @return AuthResult com sucesso ou erro
     */
    suspend fun signOut(): AuthResult

    /**
     * Obtém o usuário atualmente autenticado
     *
     * @return User se autenticado, null caso contrário
     */
    suspend fun getCurrentUser(): User?

    /**
     * Verifica se há um usuário autenticado
     *
     * @return true se há usuário autenticado, false caso contrário
     */
    suspend fun isUserAuthenticated(): Boolean
}
