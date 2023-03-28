package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.usecase.conversation.GetConversationMessages
import com.studhub.app.domain.usecase.conversation.SendMessage
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MessageRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getConversationMessages: GetConversationMessages

    @Inject
    lateinit var startConversationWith: StartConversationWith

    @Inject
    lateinit var sendMessage: SendMessage

    @Inject
    lateinit var authRepo: AuthRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun sentMessagesAreCorrectlyRetrievedInTheCorrespondingConversation() {
        val message1Content = "Hello my friend, how much for this?"
        val message2Content = "Are you here???"
        val userId = "fake-user-${Random.nextLong()}"
        val user = User(id = userId, userName = "Super User")

        lateinit var createdConversation: Conversation

        runBlocking {
            startConversationWith(user).collect {
                when (it) {
                    is ApiResponse.Failure -> fail()
                    is ApiResponse.Success -> createdConversation = it.data
                    is ApiResponse.Loading -> {}
                }
            }
        }

        val message1 = Message(content = message1Content)
        val message2 = Message(content = message2Content)

        runBlocking {
            sendMessage(createdConversation, message1).collect {
                when (it) {
                    is ApiResponse.Failure -> fail("Could not send message")
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {
                        sendMessage(createdConversation, message2).collect { it2 ->
                            when (it2) {
                                is ApiResponse.Failure -> fail("Could not send message")
                                else -> {}
                            }
                        }
                    }
                }
            }
        }

        runBlocking {
            launch {
                getConversationMessages(createdConversation).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            assertEquals(
                                "Error in the numbers of retrieved messages",
                                2,
                                it.data.size
                            )
                            assertEquals(
                                "Content of the first message do not match",
                                message1Content,
                                it.data.first().content
                            )
                            assertEquals(
                                "Content of the latest message do not match",
                                message2Content,
                                it.data.last().content
                            )
                            assertEquals(
                                "Sender of message 1 do not match logged-in user",
                                authRepo.currentUserUid,
                                it.data.first().senderId
                            )
                            assertEquals(
                                "Sender of message 2 do not match logged-in user",
                                authRepo.currentUserUid,
                                it.data.last().senderId
                            )
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
