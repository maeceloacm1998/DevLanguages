package com.dev.marcelo.devlanguages.features.onboarding.domain.usecase

import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * CompleteOnboardingUseCaseTest
 * Testes unitários para CompleteOnboardingUseCase
 */
class CompleteOnboardingUseCaseTest {

    private lateinit var repository: FakeOnboardingRepository
    private lateinit var useCase: CompleteOnboardingUseCase

    @BeforeTest
    fun setup() {
        repository = FakeOnboardingRepository()
        useCase = CompleteOnboardingUseCase(repository)
    }

    @Test
    fun `given valid userId when invoke then returns success`() = runTest {
        // Given
        val userId = "user123"

        // When
        val result = useCase(userId)

        // Then
        assertTrue(result is Result.Success)
        assertTrue(repository.completedOnboarding.contains(userId))
    }

    @Test
    fun `given empty userId when invoke then returns error`() = runTest {
        // Given
        val userId = ""

        // When
        val result = useCase(userId)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("User ID cannot be empty", (result as Result.Error).message)
    }

    @Test
    fun `given repository fails when invoke then returns error`() = runTest {
        // Given
        val userId = "user123"
        repository.shouldFail = true

        // When
        val result = useCase(userId)

        // Then
        assertTrue(result is Result.Error)
    }
}

/**
 * Fake implementation of OnboardingRepository for testing
 */
private class FakeOnboardingRepository : OnboardingRepository {
    val savedLanguages = mutableMapOf<String, String>()
    val completedOnboarding = mutableSetOf<String>()
    var shouldFail = false

    override suspend fun savePreferredLanguage(userId: String, languageCode: String): Result<Unit> {
        return if (shouldFail) {
            Result.Error("Repository error")
        } else {
            savedLanguages[userId] = languageCode
            Result.Success(Unit)
        }
    }

    override suspend fun markOnboardingComplete(userId: String): Result<Unit> {
        return if (shouldFail) {
            Result.Error("Repository error")
        } else {
            completedOnboarding.add(userId)
            Result.Success(Unit)
        }
    }

    override suspend fun hasCompletedOnboarding(userId: String): Result<Boolean> {
        return if (shouldFail) {
            Result.Error("Repository error")
        } else {
            Result.Success(completedOnboarding.contains(userId))
        }
    }
}
