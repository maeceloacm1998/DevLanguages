package com.dev.marcelo.devlanguages.features.auth.ui.login

/**
 * LoginEvent
 * Eventos da tela de login
 */
sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data object TogglePasswordVisibility : LoginEvent()
    data object SignInWithEmail : LoginEvent()
    data object SignInWithGoogle : LoginEvent()
    data object SignInWithApple : LoginEvent()
    data object SignInAnonymously : LoginEvent()
    data object NavigateToSignUp : LoginEvent()
    data object ClearError : LoginEvent()
}
