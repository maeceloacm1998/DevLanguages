package com.dev.marcelo.devlanguages.features.auth.data.di

import com.dev.marcelo.devlanguages.features.auth.data.datasource.AuthDataSource
import com.dev.marcelo.devlanguages.features.auth.data.datasource.FirebaseAuthDataSource
import com.dev.marcelo.devlanguages.features.auth.data.repository.AuthRepositoryImpl
import com.dev.marcelo.devlanguages.features.auth.domain.repository.AuthRepository
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.GetCurrentUserUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInAnonymouslyUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithAppleUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithEmailUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithGoogleUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignOutUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignUpWithEmailUseCase
import com.dev.marcelo.devlanguages.features.auth.ui.login.LoginViewModel
import com.dev.marcelo.devlanguages.features.auth.ui.signup.SignUpViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * AuthModule
 * Módulo Koin para injeção de dependências da feature de autenticação
 */
val authModule = module {
    // DataSource
    singleOf(::FirebaseAuthDataSource) bind AuthDataSource::class

    // Repository
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

    // Use Cases
    factoryOf(::SignInWithEmailUseCase)
    factoryOf(::SignUpWithEmailUseCase)
    factoryOf(::SignInWithGoogleUseCase)
    factoryOf(::SignInWithAppleUseCase)
    factoryOf(::SignInAnonymouslyUseCase)
    factoryOf(::SignOutUseCase)
    factoryOf(::GetCurrentUserUseCase)

    // ViewModels
    factoryOf(::LoginViewModel)
    factoryOf(::SignUpViewModel)
}
