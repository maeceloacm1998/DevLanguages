package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignOutUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignOutUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignOutUseCase(fakeRepository)
    }

    @Test
    fun `given successful sign out when invoke then returns success`() = runTest {
        // Given
        fakeRepository.shouldReturnError = false

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Success)
    }

    @Test
    fun `given repository error when invoke then returns error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Sign out failed"

        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Sign out failed", (result as AuthResult.Error).message)
    }
}
