package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.User
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository

/**
 * GetCurrentUserUseCase
 * Caso de uso para obter o usuário atual
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Obtém o usuário atualmente autenticado
     *
     * @return User se autenticado, null caso contrário
     */
    suspend operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}
