package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignInWithGoogleUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignInWithGoogleUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignInWithGoogleUseCase(fakeRepository)
    }

    @Test
    fun `given successful google sign in when invoke then returns success`() = runTest {
        // Given
        fakeRepository.shouldReturnError = false

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals("google@test.com", (result as AuthResult.Success).user.email)
    }

    @Test
    fun `given repository error when invoke then returns error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Google sign in failed"

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Google sign in failed", (result as AuthResult.Error).message)
    }
}
