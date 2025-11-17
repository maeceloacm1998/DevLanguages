package com.dev.marcelo.devlanguages.features.onboarding.ui.language

import com.dev.marcelo.devlanguages.features.onboarding.domain.model.Language

/**
 * LanguageSelectionEvent
 * Eventos da tela de seleção de idioma
 */
sealed class LanguageSelectionEvent {
    data class LanguageSelected(val language: Language) : LanguageSelectionEvent()
    data object Continue : LanguageSelectionEvent()
    data object ClearError : LanguageSelectionEvent()
}

/**
 * Navigation Events
 */
sealed class LanguageSelectionNavigationEvent {
    data object NavigateToTutorial : LanguageSelectionNavigationEvent()
}
