package com.dev.marcelo.devlanguages.core.di

import org.koin.core.module.Module

/**
 * App Modules
 * Lista de todos os módulos Koin do aplicativo
 */
val appModules: List<Module> = listOf(
    networkModule,
    firebaseModule
    // Adicionar outros módulos conforme forem criados:
    // authModule (UseCases, Repositories, ViewModels da feature de auth)
    // homeModule (UseCases, Repositories, ViewModels da feature de home)
    // etc...
)
