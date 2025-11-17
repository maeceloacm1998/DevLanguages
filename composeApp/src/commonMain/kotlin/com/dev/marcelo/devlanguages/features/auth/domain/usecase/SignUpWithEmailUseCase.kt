package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * SignUpWithEmailUseCase
 * Caso de uso para cadastro com email e senha
 */
class SignUpWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Executa o cadastro com email e senha
     *
     * @param email Email do usuário
     * @param password Senha do usuário
     * @param displayName Nome de exibição
     * @return AuthResult com resultado da operação
     */
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String
    ): AuthResult {
        // Validação básica
        if (email.isBlank()) {
            return AuthResult.Error("Email não pode estar vazio")
        }

        if (!email.contains("@")) {
            return AuthResult.Error("Email inválido")
        }

        if (password.isBlank()) {
            return AuthResult.Error("Senha não pode estar vazia")
        }

        if (password.length < 6) {
            return AuthResult.Error("Senha deve ter no mínimo 6 caracteres")
        }

        if (displayName.isBlank()) {
            return AuthResult.Error("Nome não pode estar vazio")
        }

        if (displayName.length < 2) {
            return AuthResult.Error("Nome deve ter no mínimo 2 caracteres")
        }

        return authRepository.signUpWithEmail(email, password, displayName)
    }
}
