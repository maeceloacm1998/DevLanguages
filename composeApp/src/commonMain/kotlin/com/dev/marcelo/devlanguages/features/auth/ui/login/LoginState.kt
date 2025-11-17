package com.dev.marcelo.devlanguages.features.auth.ui.login

/**
 * LoginState
 * Estado da tela de login
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)
