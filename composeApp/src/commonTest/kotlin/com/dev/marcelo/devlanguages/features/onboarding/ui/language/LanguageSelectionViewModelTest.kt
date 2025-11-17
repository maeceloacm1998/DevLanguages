package com.dev.marcelo.devlanguages.features.onboarding.ui.language

import app.cash.turbine.test
import com.dev.marcelo.devlanguages.core.auth.FirebaseAuthWrapper
import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.model.AvailableLanguages
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.SavePreferredLanguageUseCase
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * LanguageSelectionViewModelTest
 * Testes unitários para LanguageSelectionViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LanguageSelectionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var savePreferredLanguageUseCase: FakeSavePreferredLanguageUseCase
    private lateinit var authWrapper: FakeFirebaseAuthWrapper
    private lateinit var viewModel: LanguageSelectionViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savePreferredLanguageUseCase = FakeSavePreferredLanguageUseCase()
        authWrapper = FakeFirebaseAuthWrapper()
        viewModel = LanguageSelectionViewModel(savePreferredLanguageUseCase, authWrapper)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has all available languages`() = runTest {
        // Then
        assertEquals(AvailableLanguages.all, viewModel.state.value.availableLanguages)
        assertNull(viewModel.state.value.selectedLanguage)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `given language selected when LanguageSelected event then updates selected language`() = runTest {
        // Given
        val language = AvailableLanguages.ENGLISH

        // When
        viewModel.onEvent(LanguageSelectionEvent.LanguageSelected(language))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(language, viewModel.state.value.selectedLanguage)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `given no language selected when Continue event then shows error`() = runTest {
        // When
        viewModel.onEvent(LanguageSelectionEvent.Continue)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Por favor, selecione um idioma", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `given valid language selected when Continue event then navigates to tutorial`() = runTest {
        // Given
        val language = AvailableLanguages.SPANISH
        viewModel.onEvent(LanguageSelectionEvent.LanguageSelected(language))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(LanguageSelectionEvent.Continue)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is LanguageSelectionNavigationEvent.NavigateToTutorial)
        }
    }

    @Test
    fun `given user not authenticated when Continue event then shows error`() = runTest {
        // Given
        val language = AvailableLanguages.FRENCH
        authWrapper.setCurrentUserId(null) // Simulate not authenticated
        viewModel.onEvent(LanguageSelectionEvent.LanguageSelected(language))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(LanguageSelectionEvent.Continue)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Usuário não autenticado", viewModel.state.value.error)
    }

    @Test
    fun `given save language fails when Continue event then shows error`() = runTest {
        // Given
        val language = AvailableLanguages.GERMAN
        savePreferredLanguageUseCase.shouldFail = true
        viewModel.onEvent(LanguageSelectionEvent.LanguageSelected(language))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(LanguageSelectionEvent.Continue)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `given error when ClearError event then clears error`() = runTest {
        // Given
        viewModel.onEvent(LanguageSelectionEvent.Continue) // Trigger error
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.state.value.error)

        // When
        viewModel.onEvent(LanguageSelectionEvent.ClearError)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `when Continue event then shows loading state`() = runTest {
        // Given
        val language = AvailableLanguages.ITALIAN
        viewModel.onEvent(LanguageSelectionEvent.LanguageSelected(language))
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onEvent(LanguageSelectionEvent.Continue)

        // Then (before advancing scheduler)
        assertTrue(viewModel.state.value.isLoading)
    }
}

/**
 * Fake SavePreferredLanguageUseCase for testing
 */
private class FakeSavePreferredLanguageUseCase : SavePreferredLanguageUseCase(StubOnboardingRepository()) {
    var shouldFail = false

    override suspend fun invoke(userId: String, languageCode: String): Result<Unit> {
        return if (shouldFail) {
            Result.Error("Failed to save language")
        } else {
            Result.Success(Unit)
        }
    }
}

/**
 * Stub OnboardingRepository for testing
 */
private class StubOnboardingRepository : com.dev.marcelo.devlanguages.features.onboarding.domain.repository.OnboardingRepository {
    override suspend fun savePreferredLanguage(userId: String, languageCode: String): Result<Unit> = Result.Success(Unit)
    override suspend fun markOnboardingComplete(userId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun hasCompletedOnboarding(userId: String): Result<Boolean> = Result.Success(false)
}

/**
 * Fake FirebaseAuthWrapper for testing
 */
private class FakeFirebaseAuthWrapper : FirebaseAuthWrapper() {
    private var _currentUserId: String? = "test-user-123"

    override val currentUserId: String?
        get() = _currentUserId

    override val currentUser: FirebaseUser?
        get() = null

    override val authStateFlow: Flow<Boolean>
        get() = flowOf(_currentUserId != null)

    override val isLoggedIn: Boolean
        get() = _currentUserId != null

    override val currentUserEmail: String?
        get() = null

    override val currentUserDisplayName: String?
        get() = null

    // Helper to set currentUserId for tests
    fun setCurrentUserId(userId: String?) {
        _currentUserId = userId
    }
}
