package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignInAnonymouslyUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignInAnonymouslyUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignInAnonymouslyUseCase(fakeRepository)
    }

    @Test
    fun `given successful anonymous sign in when invoke then returns success`() = runTest {
        // Given
        fakeRepository.shouldReturnError = false

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Success)
        assertTrue((result as AuthResult.Success).user.isAnonymous)
        assertEquals("Anonymous", result.user.displayName)
    }

    @Test
    fun `given repository error when invoke then returns error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Anonymous sign in failed"

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Anonymous sign in failed", (result as AuthResult.Error).message)
    }
}
