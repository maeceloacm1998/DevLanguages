package com.dev.marcelo.devlanguages.core.di

import com.dev.marcelo.devlanguages.features.auth.data.di.authModule
import com.dev.marcelo.devlanguages.features.onboarding.data.di.onboardingModule
import org.koin.core.module.Module

/**
 * App Modules
 * Lista de todos os módulos Koin do aplicativo
 */
val appModules: List<Module> = listOf(
    networkModule,
    firebaseModule,
    authModule,
    onboardingModule
    // Adicionar outros módulos conforme forem criados:
    // homeModule (UseCases, Repositories, ViewModels da feature de home)
    // gameModule (UseCases, Repositories, ViewModels da feature de games)
    // etc...
)
