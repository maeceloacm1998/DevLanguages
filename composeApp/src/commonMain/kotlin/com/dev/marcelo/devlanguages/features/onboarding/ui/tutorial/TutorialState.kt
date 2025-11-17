package com.dev.marcelo.devlanguages.features.onboarding.ui.tutorial

import com.dev.marcelo.devlanguages.features.onboarding.domain.model.OnboardingStep

/**
 * TutorialState
 * Estado da tela de tutorial
 */
data class TutorialState(
    val steps: List<OnboardingStep> = emptyList(),
    val currentStepIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val currentStep: OnboardingStep?
        get() = steps.getOrNull(currentStepIndex)

    val isLastStep: Boolean
        get() = currentStepIndex == steps.lastIndex

    val progress: Float
        get() = if (steps.isEmpty()) 0f else (currentStepIndex + 1).toFloat() / steps.size.toFloat()
}
