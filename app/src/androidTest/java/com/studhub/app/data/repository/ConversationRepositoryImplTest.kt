package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ConversationRepository
import com.studhub.app.domain.usecase.conversation.GetCurrentUserConversations
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ConversationRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getCurrentUserConversations: GetCurrentUserConversations

    @Inject
    lateinit var startConversationWith: StartConversationWith

    @Inject
    lateinit var conversationRepository: ConversationRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun userCannotCreateConversationWithThemself() {
        var allowed = true
        val user = MockAuthRepositoryImpl.loggedInUser
        val conversation = Conversation(user1Id = user.id, user2Id = user.id)

        runBlocking {
            conversationRepository.createConversation(conversation).collect {
                when (it) {
                    is ApiResponse.Failure -> allowed = false
                    is ApiResponse.Success -> allowed = true
                    is ApiResponse.Loading -> {}
                }
            }
        }

        assertFalse(allowed)

    }

    @Test
    fun startedConversationIsCorrectlyRetrievedAfterCreation() {
        val userId = "fake-user-${Random.nextLong()}"
        val user = User(id = userId, userName = "Super User")

        runBlocking {
            startConversationWith(user).collect {
                when (it) {
                    is ApiResponse.Failure -> fail()
                    else -> {}
                }
            }
        }

        runBlocking {
            launch {
                getCurrentUserConversations().collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            assertEquals(userId, it.data.first().user2Id)
                            cancel()
                        }
                        is ApiResponse.Failure -> {
                            fail()
                            cancel()
                        }
                        is ApiResponse.Loading -> {}
                    }
                }
            }
        }

    }
}
