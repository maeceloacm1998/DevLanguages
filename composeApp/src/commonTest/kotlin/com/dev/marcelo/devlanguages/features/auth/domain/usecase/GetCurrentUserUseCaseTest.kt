package com.dev.marcelo.devlanguages.features.auth.domain.usecase

import com.dev.marcelo.devlanguages.features.auth.domain.model.User
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GetCurrentUserUseCaseTest {
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var useCase: GetCurrentUserUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeAuthRepository()
        useCase = GetCurrentUserUseCase(fakeRepository)
    }

    @Test
    fun `given authenticated user when invoke then returns user`() = runTest {
        // Given
        val testUser = User(
            id = "test-id",
            email = "test@example.com",
            displayName = "Test User",
            createdAt = Clock.System.now()
        )
        fakeRepository.currentUser = testUser

        // When
        val result = useCase()

        // Then
        assertNotNull(result)
        assertEquals(testUser.id, result.id)
        assertEquals(testUser.email, result.email)
        assertEquals(testUser.displayName, result.displayName)
    }

    @Test
    fun `given no authenticated user when invoke then returns null`() = runTest {
        // Given
        fakeRepository.currentUser = null

        // When
        val result = useCase()

        // Then
        assertNull(result)
    }
}
