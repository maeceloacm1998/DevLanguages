package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * SignInWithAppleUseCase
 * Caso de uso para login com Apple (iOS)
 */
class SignInWithAppleUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Executa o login com Apple
     *
     * @return AuthResult com resultado da operação
     */
    suspend operator fun invoke(): AuthResult {
        return authRepository.signInWithApple()
    }
}
