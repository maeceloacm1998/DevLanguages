package com.dev.marcelo.devlanguages.features.auth.domain.model

import kotlinx.datetime.Instant

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
    val createdAt: Instant? = null
)
