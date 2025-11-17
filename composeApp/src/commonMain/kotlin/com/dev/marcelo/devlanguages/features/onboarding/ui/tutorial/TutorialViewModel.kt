package com.dev.marcelo.devlanguages.features.onboarding.ui.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.marcelo.devlanguages.core.auth.FirebaseAuthWrapper
import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.model.OnboardingSteps
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.CompleteOnboardingUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * TutorialViewModel
 * ViewModel para a tela de tutorial
 */
class TutorialViewModel(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val authWrapper: FirebaseAuthWrapper
) : ViewModel() {

    private val _state = MutableStateFlow(TutorialState())
    val state: StateFlow<TutorialState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<TutorialNavigationEvent>()
    val navigationEvent: SharedFlow<TutorialNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadSteps()
    }

    /**
     * Carrega os passos do tutorial
     */
    private fun loadSteps() {
        _state.update { it.copy(steps = OnboardingSteps.all) }
    }

    /**
     * Processa eventos da UI
     */
    fun onEvent(event: TutorialEvent) {
        when (event) {
            is TutorialEvent.NextStep -> nextStep()
            is TutorialEvent.PreviousStep -> previousStep()
            is TutorialEvent.Skip -> finishOnboarding()
            is TutorialEvent.Finish -> finishOnboarding()
            is TutorialEvent.ClearError -> clearError()
        }
    }

    /**
     * Avança para o próximo passo
     */
    private fun nextStep() {
        val currentIndex = _state.value.currentStepIndex
        val maxIndex = _state.value.steps.lastIndex

        if (currentIndex < maxIndex) {
            _state.update { it.copy(currentStepIndex = currentIndex + 1) }
        } else {
            // Último passo, finalizar onboarding
            finishOnboarding()
        }
    }

    /**
     * Volta para o passo anterior
     */
    private fun previousStep() {
        val currentIndex = _state.value.currentStepIndex
        if (currentIndex > 0) {
            _state.update { it.copy(currentStepIndex = currentIndex - 1) }
        }
    }

    /**
     * Finaliza o onboarding e navega para a Home
     */
    private fun finishOnboarding() {
        val userId = authWrapper.currentUserId
        if (userId == null) {
            _state.update { it.copy(error = "Usuário não autenticado") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = completeOnboardingUseCase(userId)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(TutorialNavigationEvent.NavigateToHome)
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Limpa o erro atual
     */
    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
