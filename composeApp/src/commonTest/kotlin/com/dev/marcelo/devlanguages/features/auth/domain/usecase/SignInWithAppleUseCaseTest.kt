package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignInWithAppleUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignInWithAppleUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignInWithAppleUseCase(fakeRepository)
    }

    @Test
    fun `given successful apple sign in when invoke then returns success`() = runTest {
        // Given
        fakeRepository.shouldReturnError = false

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals("apple@test.com", (result as AuthResult.Success).user.email)
    }

    @Test
    fun `given repository error when invoke then returns error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Apple sign in failed"

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Apple sign in failed", (result as AuthResult.Error).message)
    }
}
