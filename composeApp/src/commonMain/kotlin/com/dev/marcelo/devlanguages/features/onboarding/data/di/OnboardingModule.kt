package com.dev.marcelo.devlanguages.features.onboarding.data.di

import com.dev.marcelo.devlanguages.features.onboarding.data.repository.OnboardingRepositoryImpl
import com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.CheckOnboardingStatusUseCase
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.SavePreferredLanguageUseCase
import com.dev.marcelo.devlanguages.features.onboarding.ui.language.LanguageSelectionViewModel
import com.dev.marcelo.devlanguages.features.onboarding.ui.tutorial.TutorialViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Onboarding Module
 * Módulo Koin para injeção de dependências da feature de Onboarding
 */
val onboardingModule = module {
    // Repository
    singleOf(::OnboardingRepositoryImpl) bind OnboardingRepository::class

    // Use Cases
    factoryOf(::SavePreferredLanguageUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::CheckOnboardingStatusUseCase)

    // ViewModels
    viewModelOf(::LanguageSelectionViewModel)
    viewModelOf(::TutorialViewModel)
}
