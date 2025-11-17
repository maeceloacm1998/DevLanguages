package com.dev.marcelo.devlanguages.features.auth.data.repository

import com.dev.marcelo.devlanguages.features.auth.data.datasource.AuthDataSource
import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.model.User
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository
import io.github.aakira.napier.Napier

/**
 * AuthRepositoryImpl
 * Implementação do AuthRepository usando AuthDataSource
 */
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return try {
            val result = authDataSource.signInWithEmail(email, password)
            result.fold(
                onSuccess = { user -> AuthResult.Success(user) },
                onFailure = { exception ->
                    Napier.e("Sign in with email failed", exception)
                    AuthResult.Error(
                        message = exception.message ?: "Erro ao fazer login",
                        exception = exception
                    )
                }
            )
        } catch (e: Exception) {
            Napier.e("Unexpected error in signInWithEmail", e)
            AuthResult.Error(
                message = e.message ?: "Erro inesperado",
                exception = e
            )
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): AuthResult {
        return try {
            val result = authDataSource.signUpWithEmail(email, password, displayName)
            result.fold(
                onSuccess = { user -> AuthResult.Success(user) },
                onFailure = { exception ->
                    Napier.e("Sign up with email failed", exception)
                    AuthResult.Error(
                        message = exception.message ?: "Erro ao criar conta",
                        exception = exception
                    )
                }
            )
        } catch (e: Exception) {
            Napier.e("Unexpected error in signUpWithEmail", e)
            AuthResult.Error(
                message = e.message ?: "Erro inesperado",
                exception = e
            )
        }
    }

    override suspend fun signInWithGoogle(): AuthResult {
        return try {
            val result = authDataSource.signInWithGoogle()
            result.fold(
                onSuccess = { user -> AuthResult.Success(user) },
                onFailure = { exception ->
                    Napier.e("Sign in with Google failed", exception)
                    AuthResult.Error(
                        message = exception.message ?: "Erro ao fazer login com Google",
                        exception = exception
                    )
                }
            )
        } catch (e: Exception) {
            Napier.e("Unexpected error in signInWithGoogle", e)
            AuthResult.Error(
                message = e.message ?: "Erro inesperado",
                exception = e
            )
        }
    }

    override suspend fun signInWithApple(): AuthResult {
        return try {
            val result = authDataSource.signInWithApple()
            result.fold(
                onSuccess = { user -> AuthResult.Success(user) },
                onFailure = { exception ->
                    Napier.e("Sign in with Apple failed", exception)
                    AuthResult.Error(
                        message = exception.message ?: "Erro ao fazer login com Apple",
                        exception = exception
                    )
                }
            )
        } catch (e: Exception) {
            Napier.e("Unexpected error in signInWithApple", e)
            AuthResult.Error(
                message = e.message ?: "Erro inesperado",
                exception = e
            )
        }
    }

    override suspend fun signInAnonymously(): AuthResult {
        return try {
            val result = authDataSource.signInAnonymously()
            result.fold(
                onSuccess = { user -> AuthResult.Success(user) },
                onFailure = { exception ->
                    Napier.e("Anonymous sign in failed", exception)
                    AuthResult.Error(
                        message = exception.message ?: "Erro ao fazer login anônimo",
                        exception = exception
                    )
                }
            )
        } catch (e: Exception) {
            Napier.e("Unexpected error in signInAnonymously", e)
            AuthResult.Error(
                message = e.message ?: "Erro inesperado",
                exception = e
            )
        }
    }

    override suspend fun signOut(): AuthResult {
        return try {
            val result = authDataSource.signOut()
            result.fold(
                onSuccess = {
                    AuthResult.Success(
                        User(
                            id = "",
                            email = "",
                            displayName = "",
                            isAnonymous = false
                        )
                    )
                },
                onFailure = { exception ->
                    Napier.e("Sign out failed", exception)
                    AuthResult.Error(
                        message = exception.message ?: "Erro ao fazer logout",
                        exception = exception
                    )
                }
            )
        } catch (e: Exception) {
            Napier.e("Unexpected error in signOut", e)
            AuthResult.Error(
                message = e.message ?: "Erro inesperado",
                exception = e
            )
        }
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            authDataSource.getCurrentUser()
        } catch (e: Exception) {
            Napier.e("Error getting current user", e)
            null
        }
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return try {
            authDataSource.isUserAuthenticated()
        } catch (e: Exception) {
            Napier.e("Error checking authentication", e)
            false
        }
    }
}
