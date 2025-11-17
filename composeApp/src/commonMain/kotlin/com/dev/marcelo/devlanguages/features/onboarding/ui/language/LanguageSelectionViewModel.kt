package com.dev.marcelo.devlanguages.features.onboarding.ui.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.marcelo.devlanguages.core.auth.FirebaseAuthWrapper
import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.model.AvailableLanguages
import com.dev.marcelo.devlanguages.features.onboarding.domain.model.Language
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.SavePreferredLanguageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * LanguageSelectionViewModel
 * ViewModel para a tela de seleção de idioma
 */
class LanguageSelectionViewModel(
    private val savePreferredLanguageUseCase: SavePreferredLanguageUseCase,
    private val authWrapper: FirebaseAuthWrapper
) : ViewModel() {

    private val _state = MutableStateFlow(LanguageSelectionState())
    val state: StateFlow<LanguageSelectionState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LanguageSelectionNavigationEvent>()
    val navigationEvent: SharedFlow<LanguageSelectionNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadAvailableLanguages()
    }

    /**
     * Carrega as linguagens disponíveis
     */
    private fun loadAvailableLanguages() {
        _state.update { it.copy(availableLanguages = AvailableLanguages.all) }
    }

    /**
     * Processa eventos da UI
     */
    fun onEvent(event: LanguageSelectionEvent) {
        when (event) {
            is LanguageSelectionEvent.LanguageSelected -> selectLanguage(event.language)
            is LanguageSelectionEvent.Continue -> continueToTutorial()
            is LanguageSelectionEvent.ClearError -> clearError()
        }
    }

    /**
     * Seleciona um idioma
     */
    private fun selectLanguage(language: Language) {
        _state.update { it.copy(selectedLanguage = language, error = null) }
    }

    /**
     * Continua para o tutorial após salvar o idioma escolhido
     */
    private fun continueToTutorial() {
        val selectedLanguage = _state.value.selectedLanguage
        if (selectedLanguage == null) {
            _state.update { it.copy(error = "Por favor, selecione um idioma") }
            return
        }

        val userId = authWrapper.currentUserId
        if (userId == null) {
            _state.update { it.copy(error = "Usuário não autenticado") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = savePreferredLanguageUseCase(userId, selectedLanguage.code)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(LanguageSelectionNavigationEvent.NavigateToTutorial)
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
