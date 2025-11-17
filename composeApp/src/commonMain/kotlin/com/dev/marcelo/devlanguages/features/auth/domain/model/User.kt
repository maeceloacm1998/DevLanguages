package com.dev.marcelo.devlanguages.features.auth.domain.model

/**
 * User Domain Model
 * Representa um usuário no domínio da aplicação
 */
data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val isAnonymous: Boolean = false,
    val createdAtMillis: Long? = null // Timestamp in milliseconds
)
