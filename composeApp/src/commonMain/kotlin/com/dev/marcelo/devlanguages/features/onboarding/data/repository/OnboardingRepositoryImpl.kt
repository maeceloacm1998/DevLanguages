package com.dev.marcelo.devlanguages.features.onboarding.data.repository

import com.dev.marcelo.devlanguages.core.database.FirestoreWrapper
import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository

/**
 * OnboardingRepositoryImpl
 * Implementação do repositório de onboarding usando Firestore
 */
class OnboardingRepositoryImpl(
    private val firestore: FirestoreWrapper
) : OnboardingRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FIELD_PREFERRED_LANGUAGE = "preferredLanguage"
        private const val FIELD_ONBOARDING_COMPLETE = "onboardingComplete"
    }

    override suspend fun savePreferredLanguage(userId: String, languageCode: String): Result<Unit> {
        return try {
            // Usa set com merge para criar documento se não existir ou atualizar se existir
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(
                    mapOf(FIELD_PREFERRED_LANGUAGE to languageCode),
                    merge = true
                )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save preferred language")
        }
    }

    override suspend fun markOnboardingComplete(userId: String): Result<Unit> {
        return try {
            // Usa set com merge para criar documento se não existir ou atualizar se existir
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(
                    mapOf(FIELD_ONBOARDING_COMPLETE to true),
                    merge = true
                )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to mark onboarding as complete")
        }
    }

    override suspend fun hasCompletedOnboarding(userId: String): Result<Boolean> {
        return try {
            val document = firestore.getDocument(
                collection = USERS_COLLECTION,
                documentId = userId
            )

            val isComplete = document?.get(FIELD_ONBOARDING_COMPLETE) as? Boolean ?: false
            Result.Success(isComplete)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to check onboarding status")
        }
    }
}
