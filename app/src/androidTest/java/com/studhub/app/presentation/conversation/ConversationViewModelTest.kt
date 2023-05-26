package com.studhub.app.presentation.conversation


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.data.repository.MockConversationRepositoryImpl
import com.studhub.app.data.repository.MockUserRepositoryImpl
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.usecase.conversation.GetCurrentUserConversations
import com.studhub.app.domain.usecase.user.GetCurrentUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat


@HiltAndroidTest
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class ConversationViewModelTest {

    private val mockAuthRepositoryImpl = MockAuthRepositoryImpl()
    private val mockUserRepositoryImpl = MockUserRepositoryImpl()
    private val mockConversationRepositoryImpl = MockConversationRepositoryImpl()

    private val getCurrentUser = GetCurrentUser(mockUserRepositoryImpl, mockAuthRepositoryImpl)
    private val getCurrentUserConversations = GetCurrentUserConversations(mockConversationRepositoryImpl, mockAuthRepositoryImpl)

    val viewModel = ConversationViewModel(getCurrentUser, getCurrentUserConversations)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
//Expected — Waiting for status to be reported
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_getLoggedInUser_then_currentUserIsUpdated() = runBlockingTest {
        viewModel.getLoggedInUser()
        delay(1000)
        assertTrue(viewModel.currentUser is ApiResponse.Success)
        val currentUser = (viewModel.currentUser as ApiResponse.Success).data
        // Assuming that MockAuthRepositoryImpl.loggedInUser is the expected user.
        val expectedUser = MockAuthRepositoryImpl.loggedInUser
        assertEquals(expectedUser, currentUser)
    }

//    @Test
//    fun when_getLoggedInUserConversations_then_conversationsAreUpdated() = runBlockingTest {
//        val fixedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-05-25 23:12:45")
//        viewModel.getLoggedInUserConversations()
//        delay(1000)
//        assertTrue(viewModel.conversations is ApiResponse.Success)
//        val conversations = (viewModel.conversations as ApiResponse.Success).data
//        // Assuming that the conversations of the logged in user are known and are as follows.
//        val expectedConversations = listOf(Conversation(createdAt = fixedDate, updatedAt = fixedDate))
//        assertEquals(expectedConversations, conversations)
//    }
}
