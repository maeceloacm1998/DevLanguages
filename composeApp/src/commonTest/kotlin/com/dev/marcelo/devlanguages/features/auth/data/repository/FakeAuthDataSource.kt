package com.dev.marcelo.devlanguages.features.auth.data.repository

import com.dev.marcelo.devlanguages.features.auth.data.datasource.AuthDataSource
import com.dev.marcelo.devlanguages.features.auth.domain.model.User

/**
 * FakeAuthDataSource
 * Implementação fake do AuthDataSource para testes
 */
class FakeAuthDataSource : AuthDataSource {
    var shouldReturnError = false
    var errorMessage = "Test error"
    var currentUser: User? = null

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception(errorMessage))
        } else {
            val user = User(
                id = "test-user-id",
                email = email,
                displayName = "Test User",
                createdAtMillis = null
            )
            currentUser = user
            Result.success(user)
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception(errorMessage))
        } else {
            val user = User(
                id = "new-user-id",
                email = email,
                displayName = displayName,
                createdAtMillis = null
            )
            currentUser = user
            Result.success(user)
        }
    }

    override suspend fun signInWithGoogle(): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception(errorMessage))
        } else {
            val user = User(
                id = "google-user-id",
                email = "google@test.com",
                displayName = "Google User",
                photoUrl = "https://example.com/photo.jpg",
                createdAtMillis = null
            )
            currentUser = user
            Result.success(user)
        }
    }

    override suspend fun signInWithApple(): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception(errorMessage))
        } else {
            val user = User(
                id = "apple-user-id",
                email = "apple@test.com",
                displayName = "Apple User",
                createdAtMillis = null
            )
            currentUser = user
            Result.success(user)
        }
    }

    override suspend fun signInAnonymously(): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception(errorMessage))
        } else {
            val user = User(
                id = "anon-user-id",
                email = "",
                displayName = "Anonymous",
                isAnonymous = true,
                createdAtMillis = null
            )
            currentUser = user
            Result.success(user)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception(errorMessage))
        } else {
            currentUser = null
            Result.success(Unit)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return currentUser != null
    }
}
