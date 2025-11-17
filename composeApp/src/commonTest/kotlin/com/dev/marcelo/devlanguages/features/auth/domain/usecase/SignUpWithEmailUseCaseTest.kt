package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignUpWithEmailUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignUpWithEmailUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignUpWithEmailUseCase(fakeRepository)
    }

    @Test
    fun `given valid data when invoke then returns success`() = runTest {
        // Given
        val email = "newuser@example.com"
        val password = "password123"
        val displayName = "New User"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals(email, (result as AuthResult.Success).user.email)
        assertEquals(displayName, result.user.displayName)
    }

    @Test
    fun `given empty email when invoke then returns error`() = runTest {
        // Given
        val email = ""
        val password = "password123"
        val displayName = "User"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Email não pode estar vazio", (result as AuthResult.Error).message)
    }

    @Test
    fun `given invalid email when invoke then returns error`() = runTest {
        // Given
        val email = "invalidemail"
        val password = "password123"
        val displayName = "User"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Email inválido", (result as AuthResult.Error).message)
    }

    @Test
    fun `given empty password when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = ""
        val displayName = "User"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Senha não pode estar vazia", (result as AuthResult.Error).message)
    }

    @Test
    fun `given password less than 6 chars when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "12345"
        val displayName = "User"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Senha deve ter no mínimo 6 caracteres", (result as AuthResult.Error).message)
    }

    @Test
    fun `given empty displayName when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val displayName = ""

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Nome não pode estar vazio", (result as AuthResult.Error).message)
    }

    @Test
    fun `given displayName less than 2 chars when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val displayName = "A"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Nome deve ter no mínimo 2 caracteres", (result as AuthResult.Error).message)
    }

    @Test
    fun `given repository error when invoke then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val displayName = "User"
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Email already exists"

        // When
        val result = useCase(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertEquals("Email already exists", (result as AuthResult.Error).message)
    }
}
