package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.model.User
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository
import kotlinx.datetime.Clock

/**
 * FakeAuthRepository
 * Implementação fake do AuthRepository para testes
 */
class FakeAuthRepository : AuthRepository {
    var shouldReturnError = false
    var errorMessage = "Test error"
    var currentUser: User? = null

    override suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return if (shouldReturnError) {
            AuthResult.Error(errorMessage)
        } else {
            val user = User(
                id = "test-user-id",
                email = email,
                displayName = "Test User",
                createdAt = Clock.System.now()
            )
            currentUser = user
            AuthResult.Success(user)
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): AuthResult {
        return if (shouldReturnError) {
            AuthResult.Error(errorMessage)
        } else {
            val user = User(
                id = "new-user-id",
                email = email,
                displayName = displayName,
                createdAt = Clock.System.now()
            )
            currentUser = user
            AuthResult.Success(user)
        }
    }

    override suspend fun signInWithGoogle(): AuthResult {
        return if (shouldReturnError) {
            AuthResult.Error(errorMessage)
        } else {
            val user = User(
                id = "google-user-id",
                email = "google@test.com",
                displayName = "Google User",
                photoUrl = "https://example.com/photo.jpg",
                createdAt = Clock.System.now()
            )
            currentUser = user
            AuthResult.Success(user)
        }
    }

    override suspend fun signInWithApple(): AuthResult {
        return if (shouldReturnError) {
            AuthResult.Error(errorMessage)
        } else {
            val user = User(
                id = "apple-user-id",
                email = "apple@test.com",
                displayName = "Apple User",
                createdAt = Clock.System.now()
            )
            currentUser = user
            AuthResult.Success(user)
        }
    }

    override suspend fun signInAnonymously(): AuthResult {
        return if (shouldReturnError) {
            AuthResult.Error(errorMessage)
        } else {
            val user = User(
                id = "anon-user-id",
                email = "",
                displayName = "Anonymous",
                isAnonymous = true,
                createdAt = Clock.System.now()
            )
            currentUser = user
            AuthResult.Success(user)
        }
    }

    override suspend fun signOut(): AuthResult {
        return if (shouldReturnError) {
            AuthResult.Error(errorMessage)
        } else {
            currentUser = null
            AuthResult.Success(
                User(
                    id = "",
                    email = "",
                    displayName = "",
                    createdAt = Clock.System.now()
                )
            )
        }
    }

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return currentUser != null
    }
}
