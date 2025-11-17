package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignInWithEmailUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignInWithEmailUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignInWithEmailUseCase(fakeRepository)
    }

    @Test
    fun `given valid credentials when invoke then returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals(email, (result as AuthResult.Success).user.email)
    }

    @Test
    fun `given empty email when invoke then returns error`() = runTest {
        // Given
        val email = ""
        val password = "password123"

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Email não pode estar vazio", (result as AuthResult.Error).message)
    }

    @Test
    fun `given empty password when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = ""

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Senha não pode estar vazia", (result as AuthResult.Error).message)
    }

    @Test
    fun `given password less than 6 chars when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "12345"

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Senha deve ter no mínimo 6 caracteres", (result as AuthResult.Error).message)
    }

    @Test
    fun `given repository error when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Network error"

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Network error", (result as AuthResult.Error).message)
    }
}
