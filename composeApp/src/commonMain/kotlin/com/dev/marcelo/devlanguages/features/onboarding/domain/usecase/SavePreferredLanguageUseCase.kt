package com.dev.marcelo.devlanguages.features.onboarding.domain.usecase

import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository

/**
 * SavePreferredLanguageUseCase
 * Salva a linguagem preferida do usuário no Firebase
 */
open class SavePreferredLanguageUseCase(
    private val repository: OnboardingRepository
) {
    /**
     * Executa o caso de uso
     * @param userId ID do usuário
     * @param languageCode Código da linguagem (ex: "en", "es")
     * @return Result indicando sucesso ou erro
     */
    open suspend operator fun invoke(userId: String, languageCode: String): Result<Unit> {
        // Validação do código da linguagem
        if (languageCode.isBlank()) {
            return Result.Error("Language code cannot be empty")
        }

        if (userId.isBlank()) {
            return Result.Error("User ID cannot be empty")
        }

        return repository.savePreferredLanguage(userId, languageCode)
    }
}
