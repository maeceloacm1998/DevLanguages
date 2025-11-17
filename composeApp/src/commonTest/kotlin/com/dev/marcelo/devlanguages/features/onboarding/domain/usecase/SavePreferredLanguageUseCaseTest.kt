package com.dev.marcelo.devlanguages.features.onboarding.domain.usecase

import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * SavePreferredLanguageUseCaseTest
 * Testes unitários para SavePreferredLanguageUseCase
 */
class SavePreferredLanguageUseCaseTest {

    private lateinit var repository: FakeOnboardingRepository
    private lateinit var useCase: SavePreferredLanguageUseCase

    @BeforeTest
    fun setup() {
        repository = FakeOnboardingRepository()
        useCase = SavePreferredLanguageUseCase(repository)
    }

    @Test
    fun `given valid userId and languageCode when invoke then returns success`() = runTest {
        // Given
        val userId = "user123"
        val languageCode = "en"

        // When
        val result = useCase(userId, languageCode)

        // Then
        assertTrue(result is Result.Success)
        assertTrue(repository.savedLanguages.containsKey(userId))
        assertEquals("en", repository.savedLanguages[userId])
    }

    @Test
    fun `given empty languageCode when invoke then returns error`() = runTest {
        // Given
        val userId = "user123"
        val languageCode = ""

        // When
        val result = useCase(userId, languageCode)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Language code cannot be empty", (result as Result.Error).message)
    }

    @Test
    fun `given empty userId when invoke then returns error`() = runTest {
        // Given
        val userId = ""
        val languageCode = "en"

        // When
        val result = useCase(userId, languageCode)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("User ID cannot be empty", (result as Result.Error).message)
    }

    @Test
    fun `given repository fails when invoke then returns error`() = runTest {
        // Given
        val userId = "user123"
        val languageCode = "en"
        repository.shouldFail = true

        // When
        val result = useCase(userId, languageCode)

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
