package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * SignInAnonymouslyUseCase
 * Caso de uso para login anônimo
 */
class SignInAnonymouslyUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Executa o login anônimo
     *
     * @return AuthResult com resultado da operação
     */
    suspend operator fun invoke(): AuthResult {
        return authRepository.signInAnonymously()
    }
}
