package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * SignInWithGoogleUseCase
 * Caso de uso para login com Google
 */
class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Executa o login com Google
     *
     * @return AuthResult com resultado da operação
     */
    suspend operator fun invoke(): AuthResult {
        return authRepository.signInWithGoogle()
    }
}
