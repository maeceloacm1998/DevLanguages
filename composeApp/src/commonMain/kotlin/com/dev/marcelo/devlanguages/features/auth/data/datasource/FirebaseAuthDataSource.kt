package com.dev.marcelo.devlanguages.features.auth.data.datasource

import com.dev.marcelo.devlanguages.core.auth.FirebaseAuthWrapper
import com.dev.marcelo.devlanguages.features.auth.domain.model.User
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.datetime.Clock

/**
 * FirebaseAuthDataSource
 * Implementação do AuthDataSource usando Firebase
 */
class FirebaseAuthDataSource(
    private val authWrapper: FirebaseAuthWrapper
) : AuthDataSource {

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val authResult = authWrapper.signInWithEmail(email, password)
            authResult.user?.let { firebaseUser ->
                Result.success(firebaseUser.toDomainUser())
            } ?: Result.failure(Exception("Usuário não encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        return try {
            val authResult = authWrapper.signUpWithEmail(email, password)
            // Atualizar displayName após criar a conta
            authWrapper.updateProfile(displayName, null)
            authResult.user?.let { firebaseUser ->
                Result.success(firebaseUser.toDomainUser().copy(displayName = displayName))
            } ?: Result.failure(Exception("Erro ao criar usuário"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(): Result<User> {
        return try {
            // TODO: Implementar Google Sign-In quando adicionar dependência
            // Por enquanto retorna erro
            Result.failure(Exception("Google Sign-In não implementado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithApple(): Result<User> {
        return try {
            // TODO: Implementar Apple Sign-In quando adicionar dependência
            // Por enquanto retorna erro
            Result.failure(Exception("Apple Sign-In não implementado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(): Result<User> {
        return try {
            val authResult = authWrapper.signInAnonymously()
            authResult.user?.let { firebaseUser ->
                Result.success(firebaseUser.toDomainUser())
            } ?: Result.failure(Exception("Erro ao fazer login anônimo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            authWrapper.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return authWrapper.currentUser?.toDomainUser()
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return authWrapper.currentUser != null
    }

    /**
     * Converte FirebaseUser para User do domínio
     */
    private fun FirebaseUser.toDomainUser(): User {
        return User(
            id = uid,
            email = email ?: "",
            displayName = displayName ?: "",
            photoUrl = photoURL,
            isAnonymous = isAnonymous,
            createdAt = Clock.System.now()
        )
    }
}
