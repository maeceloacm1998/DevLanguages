package com.dev.marcelo.devlanguages.features.onboarding.ui.language

import com.dev.marcelo.devlanguages.features.onboarding.domain.model.Language

/**
 * LanguageSelectionState
 * Estado da tela de seleção de idioma
 */
data class LanguageSelectionState(
    val availableLanguages: List<Language> = emptyList(),
    val selectedLanguage: Language? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
