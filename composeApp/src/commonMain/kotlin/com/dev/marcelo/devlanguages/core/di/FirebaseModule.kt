package com.dev.marcelo.devlanguages.core.di

import com.dev.marcelo.devlanguages.core.auth.FirebaseAuthWrapper
import com.dev.marcelo.devlanguages.core.database.FirestoreWrapper
import org.koin.dsl.module

/**
 * Firebase Koin Module
 * Módulo de injeção de dependência para Firebase
 */
val firebaseModule = module {
    // Firebase Auth
    single { FirebaseAuthWrapper() }

    // Firestore
    single { FirestoreWrapper() }
}
