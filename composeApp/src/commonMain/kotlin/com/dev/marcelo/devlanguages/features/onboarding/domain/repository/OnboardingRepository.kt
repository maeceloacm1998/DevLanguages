package com.dev.marcelo.devlanguages.features.onboarding.domain.repository

import com.dev.marcelo.devlanguages.core.utils.Result

/**
 * OnboardingRepository
 * Interface do repositório de onboarding (domain layer)
 */
interface OnboardingRepository {
    /**
     * Salva a linguagem preferida do usuário
     * @param userId ID do usuário
     * @param languageCode Código da linguagem escolhida (ex: "en", "es")
     * @return Result indicando sucesso ou erro
     */
    suspend fun savePreferredLanguage(userId: String, languageCode: String): Result<Unit>

    /**
     * Marca o onboarding como completo para o usuário
     * @param userId ID do usuário
     * @return Result indicando sucesso ou erro
     */
    suspend fun markOnboardingComplete(userId: String): Result<Unit>

    /**
     * Verifica se o usuário já completou o onboarding
     * @param userId ID do usuário
     * @return Result<Boolean> - true se completou, false caso contrário
     */
    suspend fun hasCompletedOnboarding(userId: String): Result<Boolean>
}
