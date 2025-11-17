package com.dev.marcelo.devlanguages.features.auth.ui.signup

import app.cash.turbine.test
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.FakeAuthRepository
import com.dev.marcelo.devlanguages.features.auth.domain.usecase.SignUpWithEmailUseCase
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
class SignUpViewModelTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var signUpWithEmailUseCase: SignUpWithEmailUseCase
    private lateinit var viewModel: SignUpViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakeAuthRepository()
        signUpWithEmailUseCase = SignUpWithEmailUseCase(fakeRepository)

        viewModel = SignUpViewModel(signUpWithEmailUseCase)
    }

    @Test
    fun `given email changed event when invoked then state updates email`() = runTest {
        // Given
        val newEmail = "test@example.com"

        // When
        viewModel.onEvent(SignUpEvent.EmailChanged(newEmail))
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
        viewModel.onEvent(SignUpEvent.PasswordChanged(newPassword))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newPassword, viewModel.state.value.password)
        assertNull(viewModel.state.value.passwordError)
    }

    @Test
    fun `given confirm password changed event when invoked then state updates confirm password`() =
        runTest {
            // Given
            val newConfirmPassword = "password123"

            // When
            viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(newConfirmPassword))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(newConfirmPassword, viewModel.state.value.confirmPassword)
            assertNull(viewModel.state.value.confirmPasswordError)
        }

    @Test
    fun `given display name changed event when invoked then state updates display name`() = runTest {
        // Given
        val newDisplayName = "Test User"

        // When
        viewModel.onEvent(SignUpEvent.DisplayNameChanged(newDisplayName))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newDisplayName, viewModel.state.value.displayName)
        assertNull(viewModel.state.value.displayNameError)
    }

    @Test
    fun `given toggle password visibility event when invoked then state toggles password visibility`() =
        runTest {
            // Given
            val initialVisibility = viewModel.state.value.isPasswordVisible

            // When
            viewModel.onEvent(SignUpEvent.TogglePasswordVisibility)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(!initialVisibility, viewModel.state.value.isPasswordVisible)
        }

    @Test
    fun `given toggle confirm password visibility event when invoked then state toggles confirm password visibility`() =
        runTest {
            // Given
            val initialVisibility = viewModel.state.value.isConfirmPasswordVisible

            // When
            viewModel.onEvent(SignUpEvent.ToggleConfirmPasswordVisibility)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(!initialVisibility, viewModel.state.value.isConfirmPasswordVisible)
        }

    @Test
    fun `given valid credentials when sign up then navigates to home`() = runTest {
        // Given
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged("Test User"))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(SignUpEvent.SignUp)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is SignUpNavigationEvent.NavigateToHome)
            assertFalse(viewModel.state.value.isLoading)
            assertNull(viewModel.state.value.error)
        }
    }

    @Test
    fun `given mismatched passwords when sign up then shows error`() = runTest {
        // Given
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged("different_password"))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged("Test User"))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(SignUpEvent.SignUp)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.confirmPasswordError)
        assertTrue(viewModel.state.value.confirmPasswordError!!.contains("não coincidem"))
    }

    @Test
    fun `given empty email when sign up then shows error`() = runTest {
        // Given
        viewModel.onEvent(SignUpEvent.EmailChanged(""))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged("Test User"))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(SignUpEvent.SignUp)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
    }

    @Test
    fun `given empty password when sign up then shows error`() = runTest {
        // Given
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged(""))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(""))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged("Test User"))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(SignUpEvent.SignUp)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
    }

    @Test
    fun `given empty display name when sign up then shows error`() = runTest {
        // Given
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(SignUpEvent.SignUp)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
    }

    @Test
    fun `given repository error when sign up then shows error`() = runTest {
        // Given
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged("Test User"))
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Email already in use"
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(SignUpEvent.SignUp)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.error!!.contains("Email already in use"))
    }

    @Test
    fun `given navigate to login event when invoked then emits navigation event`() = runTest {
        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(SignUpEvent.NavigateToLogin)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is SignUpNavigationEvent.NavigateToLogin)
        }
    }

    @Test
    fun `given clear error event when invoked then clears all errors`() = runTest {
        // Given - Set mismatched passwords to trigger error
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged("different"))
        viewModel.onEvent(SignUpEvent.DisplayNameChanged("Test User"))
        viewModel.onEvent(SignUpEvent.SignUp)
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.state.value.confirmPasswordError)

        // When
        viewModel.onEvent(SignUpEvent.ClearError)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.state.value.error)
        assertNull(viewModel.state.value.emailError)
        assertNull(viewModel.state.value.passwordError)
        assertNull(viewModel.state.value.confirmPasswordError)
        assertNull(viewModel.state.value.displayNameError)
    }
}
