package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * SignInWithEmailUseCase
 * Caso de uso para login com email e senha
 */
class SignInWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Executa o login com email e senha
     *
     * @param email Email do usuário
     * @param password Senha do usuário
     * @return AuthResult com resultado da operação
     */
    suspend operator fun invoke(email: String, password: String): AuthResult {
        // Validação básica
        if (email.isBlank()) {
            return AuthResult.Error("Email não pode estar vazio")
        }

        if (password.isBlank()) {
            return AuthResult.Error("Senha não pode estar vazia")
        }

        if (password.length < 6) {
            return AuthResult.Error("Senha deve ter no mínimo 6 caracteres")
        }

        return authRepository.signInWithEmail(email, password)
    }
}
