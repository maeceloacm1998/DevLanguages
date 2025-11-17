package com.dev.marcelo.devlanguages.features.onboarding.domain.usecase

import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository

/**
 * CheckOnboardingStatusUseCase
 * Verifica se o usuário já completou o onboarding
 */
class CheckOnboardingStatusUseCase(
    private val repository: OnboardingRepository
) {
    /**
     * Executa o caso de uso
     * @param userId ID do usuário
     * @return Result<Boolean> - true se completou, false caso contrário
     */
    suspend operator fun invoke(userId: String): Result<Boolean> {
        if (userId.isBlank()) {
            return Result.Error("User ID cannot be empty")
        }

        return repository.hasCompletedOnboarding(userId)
    }
}
