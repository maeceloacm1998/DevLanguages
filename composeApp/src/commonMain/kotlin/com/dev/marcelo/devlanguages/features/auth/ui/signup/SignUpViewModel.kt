package com.dev.marcelo.devlanguages.features.auth.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignUpWithEmailUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpNavigationEvent>()
    val navigationEvent: SharedFlow<SignUpNavigationEvent> = _navigationEvent.asSharedFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, emailError = null, error = null) }
            }

            is SignUpEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, passwordError = null, error = null) }
            }

            is SignUpEvent.ConfirmPasswordChanged -> {
                _state.update {
                    it.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null, error = null)
                }
            }

            is SignUpEvent.DisplayNameChanged -> {
                _state.update { it.copy(displayName = event.displayName, displayNameError = null, error = null) }
            }

            is SignUpEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignUpEvent.ToggleConfirmPasswordVisibility -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            is SignUpEvent.SignUp -> signUp()
            is SignUpEvent.NavigateToLogin -> navigateToLogin()
            is SignUpEvent.ClearError -> {
                _state.update {
                    it.copy(
                        error = null,
                        emailError = null,
                        passwordError = null,
                        confirmPasswordError = null,
                        displayNameError = null
                    )
                }
            }
        }
    }

    private fun signUp() {
        // Validar senhas iguais
        if (_state.value.password != _state.value.confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "As senhas não coincidem") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signUpWithEmailUseCase(
                email = _state.value.email,
                password = _state.value.password,
                displayName = _state.value.displayName
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is AuthResult.Success -> {
                    Napier.i("Sign up successful for user: ${result.user.email}")
                    _navigationEvent.emit(SignUpNavigationEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Napier.e("Sign up failed: ${result.message}")
                    _state.update { it.copy(error = result.message) }
                }

                is AuthResult.Loading -> {
                    // Não deve acontecer aqui
                }
            }
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(SignUpNavigationEvent.NavigateToLogin)
        }
    }
}

sealed class SignUpNavigationEvent {
    data object NavigateToHome : SignUpNavigationEvent()
    data object NavigateToLogin : SignUpNavigationEvent()
}
