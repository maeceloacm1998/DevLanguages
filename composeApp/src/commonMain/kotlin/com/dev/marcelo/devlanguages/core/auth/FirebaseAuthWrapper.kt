package com.dev.marcelo.devlanguages.core.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Firebase Authentication Wrapper
 * Wrapper para facilitar o uso do Firebase Auth em KMP
 */
class FirebaseAuthWrapper {
    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Usuário atual autenticado (null se não estiver logado)
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Flow do estado de autenticação
     * Emite true quando logado, false quando deslogado
     */
    val authStateFlow: Flow<Boolean> = auth.authStateChanged.map { user ->
        user != null
    }

    /**
     * Cadastro com email e senha
     */
    suspend fun signUpWithEmail(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    /**
     * Login com email e senha
     */
    suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password)
    }

    /**
     * Login anônimo (para testes ou modo guest)
     */
    suspend fun signInAnonymously(): AuthResult {
        return auth.signInAnonymously()
    }

    /**
     * Logout
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Envia email de redefinição de senha
     */
    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    /**
     * Atualiza perfil do usuário
     */
    suspend fun updateProfile(displayName: String? = null, photoUrl: String? = null) {
        currentUser?.updateProfile(displayName, photoUrl)
    }

    /**
     * Deleta usuário atual
     */
    suspend fun deleteUser() {
        currentUser?.delete()
    }

    /**
     * Verifica se está logado
     */
    val isLoggedIn: Boolean
        get() = currentUser != null

    /**
     * UID do usuário atual
     */
    val currentUserId: String?
        get() = currentUser?.uid

    /**
     * Email do usuário atual
     */
    val currentUserEmail: String?
        get() = currentUser?.email

    /**
     * Nome de exibição do usuário atual
     */
    val currentUserDisplayName: String?
        get() = currentUser?.displayName
}
