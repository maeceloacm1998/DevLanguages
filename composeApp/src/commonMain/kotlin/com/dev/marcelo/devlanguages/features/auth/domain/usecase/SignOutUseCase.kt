package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * SignOutUseCase
 * Caso de uso para logout
 */
class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Executa o logout
     *
     * @return AuthResult com resultado da operação
     */
    suspend operator fun invoke(): AuthResult {
        return authRepository.signOut()
    }
}
