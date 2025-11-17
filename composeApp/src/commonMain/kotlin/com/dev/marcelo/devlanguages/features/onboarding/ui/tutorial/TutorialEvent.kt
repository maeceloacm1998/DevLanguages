package com.dev.marcelo.devlanguages.features.onboarding.ui.tutorial

/**
 * TutorialEvent
 * Eventos da tela de tutorial
 */
sealed class TutorialEvent {
    data object NextStep : TutorialEvent()
    data object PreviousStep : TutorialEvent()
    data object Skip : TutorialEvent()
    data object Finish : TutorialEvent()
    data object ClearError : TutorialEvent()
}

/**
 * Navigation Events
 */
sealed class TutorialNavigationEvent {
    data object NavigateToHome : TutorialNavigationEvent()
}
