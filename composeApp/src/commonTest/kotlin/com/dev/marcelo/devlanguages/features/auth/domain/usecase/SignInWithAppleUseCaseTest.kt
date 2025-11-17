package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.AuthResult
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignInWithAppleUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: SignInWithAppleUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = SignInWithAppleUseCase(fakeRepository)
    }

    @Test
    fun `given apple sign in not implemented when invoke then returns error`() = runTest {
        // When
        val result = useCase()

        // Then
        assertTrue(result is AuthResult.Error)
        assertTrue((result as AuthResult.Error).message.contains("não implementado"))
    }
}
