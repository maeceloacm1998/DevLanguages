package com.dev.marcelo.devlanguages.features.onboarding.ui.tutorial

import app.cash.turbine.test
import com.dev.marcelo.devlanguages.core.auth.FirebaseAuthWrapper
import com.dev.marcelo.devlanguages.core.utils.Result
import com.dev.marcelo.devlanguages.features.onboarding.domain.model.OnboardingSteps
import com.dev.marcelo.devlanguages.features.onboarding.domain.usecase.CompleteOnboardingUseCase
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
 * TutorialViewModelTest
 * Testes unitários para TutorialViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TutorialViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var completeOnboardingUseCase: FakeCompleteOnboardingUseCase
    private lateinit var authWrapper: FakeFirebaseAuthWrapper
    private lateinit var viewModel: TutorialViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        completeOnboardingUseCase = FakeCompleteOnboardingUseCase()
        authWrapper = FakeFirebaseAuthWrapper()
        viewModel = TutorialViewModel(completeOnboardingUseCase, authWrapper)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has all tutorial steps`() = runTest {
        // Then
        assertEquals(OnboardingSteps.all, viewModel.state.value.steps)
        assertEquals(0, viewModel.state.value.currentStepIndex)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `when NextStep event then advances to next step`() = runTest {
        // Given
        val initialIndex = 0

        // When
        viewModel.onEvent(TutorialEvent.NextStep)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(initialIndex + 1, viewModel.state.value.currentStepIndex)
    }

    @Test
    fun `when PreviousStep event then goes back to previous step`() = runTest {
        // Given - advance to step 2
        viewModel.onEvent(TutorialEvent.NextStep)
        viewModel.onEvent(TutorialEvent.NextStep)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, viewModel.state.value.currentStepIndex)

        // When
        viewModel.onEvent(TutorialEvent.PreviousStep)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.state.value.currentStepIndex)
    }

    @Test
    fun `given first step when PreviousStep event then stays at first step`() = runTest {
        // Given
        assertEquals(0, viewModel.state.value.currentStepIndex)

        // When
        viewModel.onEvent(TutorialEvent.PreviousStep)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(0, viewModel.state.value.currentStepIndex)
    }

    @Test
    fun `given last step when NextStep event then finishes onboarding`() = runTest {
        // Given - navigate to last step
        val lastStepIndex = OnboardingSteps.all.lastIndex
        repeat(lastStepIndex) {
            viewModel.onEvent(TutorialEvent.NextStep)
            testDispatcher.scheduler.advanceUntilIdle()
        }
        assertEquals(lastStepIndex, viewModel.state.value.currentStepIndex)

        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(TutorialEvent.NextStep)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is TutorialNavigationEvent.NavigateToHome)
        }
    }

    @Test
    fun `when Skip event then finishes onboarding`() = runTest {
        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(TutorialEvent.Skip)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is TutorialNavigationEvent.NavigateToHome)
        }
    }

    @Test
    fun `when Finish event then finishes onboarding`() = runTest {
        // When
        viewModel.navigationEvent.test {
            viewModel.onEvent(TutorialEvent.Finish)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val event = awaitItem()
            assertTrue(event is TutorialNavigationEvent.NavigateToHome)
        }
    }

    @Test
    fun `given user not authenticated when Finish event then shows error`() = runTest {
        // Given
        authWrapper.setCurrentUserId(null) // Simulate not authenticated

        // When
        viewModel.onEvent(TutorialEvent.Finish)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Usuário não autenticado", viewModel.state.value.error)
    }

    @Test
    fun `given complete onboarding fails when Finish event then shows error`() = runTest {
        // Given
        completeOnboardingUseCase.shouldFail = true

        // When
        viewModel.onEvent(TutorialEvent.Finish)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `given error when ClearError event then clears error`() = runTest {
        // Given
        authWrapper.setCurrentUserId(null)
        viewModel.onEvent(TutorialEvent.Finish) // Trigger error
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.state.value.error)

        // When
        viewModel.onEvent(TutorialEvent.ClearError)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `state progress is calculated correctly`() = runTest {
        // Given - initial state
        assertEquals(0.25f, viewModel.state.value.progress) // 1/4 steps

        // When - advance to step 2
        viewModel.onEvent(TutorialEvent.NextStep)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(0.5f, viewModel.state.value.progress) // 2/4 steps
    }

    @Test
    fun `state isLastStep is correct`() = runTest {
        // Given - initial state
        assertFalse(viewModel.state.value.isLastStep)

        // When - navigate to last step
        val lastStepIndex = OnboardingSteps.all.lastIndex
        repeat(lastStepIndex) {
            viewModel.onEvent(TutorialEvent.NextStep)
            testDispatcher.scheduler.advanceUntilIdle()
        }

        // Then
        assertTrue(viewModel.state.value.isLastStep)
    }
}

/**
 * Fake CompleteOnboardingUseCase for testing
 */
private class FakeCompleteOnboardingUseCase : CompleteOnboardingUseCase(StubOnboardingRepository()) {
    var shouldFail = false

    override suspend fun invoke(userId: String): Result<Unit> {
        return if (shouldFail) {
            Result.Error("Failed to complete onboarding")
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
