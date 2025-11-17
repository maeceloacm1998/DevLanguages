package com.dev.marcelo.devlanguages.features.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInAnonymouslyUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithAppleUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithEmailUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithGoogleUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * LoginViewModel
 * ViewModel da tela de login
 */
class LoginViewModel(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithAppleUseCase: SignInWithAppleUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, emailError = null, error = null) }
            }

            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, passwordError = null, error = null) }
            }

            is LoginEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is LoginEvent.SignInWithEmail -> signInWithEmail()
            is LoginEvent.SignInWithGoogle -> signInWithGoogle()
            is LoginEvent.SignInWithApple -> signInWithApple()
            is LoginEvent.SignInAnonymously -> signInAnonymously()
            is LoginEvent.NavigateToSignUp -> navigateToSignUp()
            is LoginEvent.ClearError -> {
                _state.update { it.copy(error = null, emailError = null, passwordError = null) }
            }
        }
    }

    private fun signInWithEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signInWithEmailUseCase(
                email = _state.value.email,
                password = _state.value.password
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is AuthResult.Success -> {
                    Napier.i("Login successful for user: ${result.user.email}")
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Napier.e("Login failed: ${result.message}")
                    _state.update { it.copy(error = result.message) }
                }

                is AuthResult.Loading -> {
                    // Não deve acontecer aqui
                }
            }
        }
    }

    private fun signInWithGoogle() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signInWithGoogleUseCase()

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is AuthResult.Success -> {
                    Napier.i("Google login successful for user: ${result.user.email}")
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Napier.e("Google login failed: ${result.message}")
                    _state.update { it.copy(error = result.message) }
                }

                is AuthResult.Loading -> {
                    // Não deve acontecer aqui
                }
            }
        }
    }

    private fun signInWithApple() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signInWithAppleUseCase()

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is AuthResult.Success -> {
                    Napier.i("Apple login successful for user: ${result.user.email}")
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Napier.e("Apple login failed: ${result.message}")
                    _state.update { it.copy(error = result.message) }
                }

                is AuthResult.Loading -> {
                    // Não deve acontecer aqui
                }
            }
        }
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signInAnonymouslyUseCase()

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is AuthResult.Success -> {
                    Napier.i("Anonymous login successful")
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Napier.e("Anonymous login failed: ${result.message}")
                    _state.update { it.copy(error = result.message) }
                }

                is AuthResult.Loading -> {
                    // Não deve acontecer aqui
                }
            }
        }
    }

    private fun navigateToSignUp() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToSignUp)
        }
    }
}

/**
 * LoginNavigationEvent
 * Eventos de navegação da tela de login
 */
sealed class LoginNavigationEvent {
    data object NavigateToHome : LoginNavigationEvent()
    data object NavigateToSignUp : LoginNavigationEvent()
}
