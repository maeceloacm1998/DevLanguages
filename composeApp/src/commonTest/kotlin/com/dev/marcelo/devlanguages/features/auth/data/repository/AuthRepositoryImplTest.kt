package com.dev.marcelo.devlanguages.features.auth.data.repository

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryImplTest {
    private lateinit var fakeDataSource: FakeAuthDataSource
    private lateinit var repository: AuthRepositoryImpl

    @BeforeTest
    fun setup() {
        fakeDataSource = FakeAuthDataSource()
        repository = AuthRepositoryImpl(fakeDataSource)
    }

    // SignInWithEmail Tests
    @Test
    fun `given valid credentials when signInWithEmail then returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"

        // When
        val result = repository.signInWithEmail(email, password)

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals(email, (result as AuthResult.Success).user.email)
    }

    @Test
    fun `given data source error when signInWithEmail then returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeDataSource.shouldReturnError = true
        fakeDataSource.errorMessage = "Invalid credentials"

        // When
        val result = repository.signInWithEmail(email, password)

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("Invalid credentials"))
    }

    // SignUpWithEmail Tests
    @Test
    fun `given valid data when signUpWithEmail then returns success`() = runTest {
        // Given
        val email = "newuser@example.com"
        val password = "password123"
        val displayName = "New User"

        // When
        val result = repository.signUpWithEmail(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals(email, (result as AuthResult.Success).user.email)
        assertEquals(displayName, result.user.displayName)
    }

    @Test
    fun `given data source error when signUpWithEmail then returns error`() = runTest {
        // Given
        val email = "newuser@example.com"
        val password = "password123"
        val displayName = "New User"
        fakeDataSource.shouldReturnError = true
        fakeDataSource.errorMessage = "Email already exists"

        // When
        val result = repository.signUpWithEmail(email, password, displayName)

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("Email already exists"))
    }

    // SignInWithGoogle Tests
    @Test
    fun `given successful google auth when signInWithGoogle then returns success`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = false

        // When
        val result = repository.signInWithGoogle()

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals("google@test.com", (result as AuthResult.Success).user.email)
    }

    @Test
    fun `given data source error when signInWithGoogle then returns error`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = true
        fakeDataSource.errorMessage = "Google sign in cancelled"

        // When
        val result = repository.signInWithGoogle()

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("Google sign in cancelled"))
    }

    // SignInWithApple Tests
    @Test
    fun `given successful apple auth when signInWithApple then returns success`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = false

        // When
        val result = repository.signInWithApple()

        // Then
        assertTrue(result is AuthResult.Success)
        assertEquals("apple@test.com", (result as AuthResult.Success).user.email)
    }

    @Test
    fun `given data source error when signInWithApple then returns error`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = true
        fakeDataSource.errorMessage = "Apple sign in failed"

        // When
        val result = repository.signInWithApple()

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("Apple sign in failed"))
    }

    // SignInAnonymously Tests
    @Test
    fun `given successful anonymous auth when signInAnonymously then returns success`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = false

        // When
        val result = repository.signInAnonymously()

        // Then
        assertTrue(result is AuthResult.Success)
        assertTrue((result as AuthResult.Success).user.isAnonymous)
    }

    @Test
    fun `given data source error when signInAnonymously then returns error`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = true
        fakeDataSource.errorMessage = "Anonymous sign in failed"

        // When
        val result = repository.signInAnonymously()

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("Anonymous sign in failed"))
    }

    // SignOut Tests
    @Test
    fun `given successful sign out when signOut then returns success`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = false

        // When
        val result = repository.signOut()

        // Then
        assertTrue(result is AuthResult.Success)
    }

    @Test
    fun `given data source error when signOut then returns error`() = runTest {
        // Given
        fakeDataSource.shouldReturnError = true
        fakeDataSource.errorMessage = "Sign out failed"

        // When
        val result = repository.signOut()

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("Sign out failed"))
    }

    // GetCurrentUser Tests
    @Test
    fun `given authenticated user when getCurrentUser then returns user`() = runTest {
        // Given
        fakeDataSource.signInWithEmail("test@example.com", "password")

        // When
        val user = repository.getCurrentUser()

        // Then
        assertNotNull(user)
        assertEquals("test@example.com", user.email)
    }

    @Test
    fun `given no authenticated user when getCurrentUser then returns null`() = runTest {
        // Given
        fakeDataSource.currentUser = null

        // When
        val user = repository.getCurrentUser()

        // Then
        assertNull(user)
    }

    // IsUserAuthenticated Tests
    @Test
    fun `given authenticated user when isUserAuthenticated then returns true`() = runTest {
        // Given
        fakeDataSource.signInWithEmail("test@example.com", "password")

        // When
        val isAuthenticated = repository.isUserAuthenticated()

        // Then
        assertTrue(isAuthenticated)
    }

    @Test
    fun `given no authenticated user when isUserAuthenticated then returns false`() = runTest {
        // Given
        fakeDataSource.currentUser = null

        // When
        val isAuthenticated = repository.isUserAuthenticated()

        // Then
        assertTrue(!isAuthenticated)
    }
}
