package com.dev.marcelo.devlanguages.features.auth.ui.login

import app.cash.turbine.test
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.FakeAuthRepository
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInAnonymouslyUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithAppleUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithEmailUseCase
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase
    private lateinit var signInWithGoogleUseCase: SignInWithGoogleUseCase
    private lateinit var signInWithAppleUseCase: SignInWithAppleUseCase
    private lateinit var signInAnonymouslyUseCase: SignInAnonymouslyUseCase
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakeAuthRepository()
        signInWithEmailUseCase = SignInWithEmailUseCase(fakeRepository)
        signInWithGoogleUseCase = SignInWithGoogleUseCase(fakeRepository)
        signInWithAppleUseCase = SignInWithAppleUseCase(fakeRepository)
        signInAnonymouslyUseCase = SignInAnonymouslyUseCase(fakeRepository)

        viewModel = LoginViewModel(
            signInWithEmailUseCase,
            signInWithGoogleUseCase,
            signInWithAppleUseCase,
            signInAnonymouslyUseCase
        )
    }

    @Test
    fun `given email changed event when invoked then state updates email`() = runTest {
        // Given
        val newEmail = "test@example.com"

        // When
        viewModel.onEvent(LoginEvent.EmailChanged(newEmail))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newEmail, viewModel.state.value.email)
        assertNull(viewModel.state.value.emailError)
    }

    @Test
    fun `given password changed event when invoked then state updates password`() = runTest {
        // Given
        val newPassword = "password123"

        // When
        viewModel.onEvent(LoginEvent.PasswordChanged(newPassword))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newPassword, viewModel.state.value.password)
        assertNull(viewModel.state.value.passwordError)
    }

    @Test
    fun `given toggle password visibility event when invoked then state toggles visibility`() =
        runTest {
            // Given
            val initialVisibility = viewModel.state.value.isPasswordVisible

            // When
            viewModel.onEvent(LoginEvent.TogglePasswordVisibility)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(!initialVisibility, viewModel.state.value.isPasswordVisible)
        }

    @Test
    fun `given valid credentials when sign in with email then navigates to home`() = runTest {
        // Given
        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(LoginEvent.SignInWithEmail)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is LoginNavigationEvent.NavigateToHome)
            assertFalse(viewModel.state.value.isLoading)
            assertNull(viewModel.state.value.error)
        }
    }

    @Test
    fun `given empty email when sign in with email then shows error`() = runTest {
        // Given
        viewModel.onEvent(LoginEvent.EmailChanged(""))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(LoginEvent.SignInWithEmail)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.error!!.contains("vazio"))
    }

    @Test
    fun `given empty password when sign in with email then shows error`() = runTest {
        // Given
        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(LoginEvent.SignInWithEmail)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.error!!.contains("vazio") || viewModel.state.value.error!!.contains("vazia"))
    }

    @Test
    fun `given repository error when sign in with email then shows error`() = runTest {
        // Given
        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Invalid credentials"
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(LoginEvent.SignInWithEmail)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.error!!.contains("Invalid credentials"))
    }

    @Test
    fun `given sign in with google event when invoked then shows error`() = runTest {
        // When
        viewModel.onEvent(LoginEvent.SignInWithGoogle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
    }

    @Test
    fun `given sign in with apple event when invoked then shows error`() = runTest {
        // When
        viewModel.onEvent(LoginEvent.SignInWithApple)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
    }

    @Test
    fun `given sign in anonymously event when invoked then navigates to home`() = runTest {
        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(LoginEvent.SignInAnonymously)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is LoginNavigationEvent.NavigateToHome)
            assertFalse(viewModel.state.value.isLoading)
        }
    }

    @Test
    fun `given navigate to sign up event when invoked then emits navigation event`() = runTest {
        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(LoginEvent.NavigateToSignUp)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is LoginNavigationEvent.NavigateToSignUp)
        }
    }

    @Test
    fun `given clear error event when invoked then clears all errors`() = runTest {
        // Given
        viewModel.onEvent(LoginEvent.EmailChanged(""))
        viewModel.onEvent(LoginEvent.SignInWithEmail)
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.state.value.error)

        // When
        viewModel.onEvent(LoginEvent.ClearError)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.state.value.error)
        assertNull(viewModel.state.value.emailError)
        assertNull(viewModel.state.value.passwordError)
    }
}
