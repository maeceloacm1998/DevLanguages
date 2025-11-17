package com.dev.marcelo.devlanguages.features.onboarding.domain.usecase

import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository

/**
 * CompleteOnboardingUseCase
 * Marca o onboarding como completo para o usuário
 */
open class CompleteOnboardingUseCase(
    private val repository: OnboardingRepository
) {
    /**
     * Executa o caso de uso
     * @param userId ID do usuário
     * @return Result indicando sucesso ou erro
     */
    open suspend operator fun invoke(userId: String): Result<Unit> {
        if (userId.isBlank()) {
            return Result.Error("User ID cannot be empty")
        }

        return repository.markOnboardingComplete(userId)
    }
}
