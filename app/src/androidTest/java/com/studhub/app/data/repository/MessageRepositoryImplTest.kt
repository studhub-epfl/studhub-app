package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.ConversationRepository
import com.studhub.app.domain.repository.MessageRepository
import com.studhub.app.domain.usecase.conversation.SendMessage
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
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MessageRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var messageRepository: MessageRepository

    @Inject
    lateinit var conversationRepository: ConversationRepository

    @Inject
    lateinit var sendMessage: SendMessage

    @Inject
    lateinit var localDb: LocalAppDatabase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun bufferedMessagesAreCorrectlyFlushed() {
        val cache = LocalDataSource(localDb)
        val senderId = "Bobby-${Random.nextLong()}"
        lateinit var conversation: Conversation

        // create fake conversation
        runBlocking {
            conversationRepository.createConversation(
                Conversation(
                    user1Id = senderId,
                    user2Id = "Maya"
                )
            ).collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Failure -> fail()
                    is ApiResponse.Success -> conversation = it.data
                }
            }
        }

        val message1 = Message(
            conversationId = conversation.id,
            senderId = senderId,
            createdAt = Date(1000),
            content = Random.nextLong().toString()
        )
        val message2 = Message(
            conversationId = conversation.id,
            senderId = senderId,
            createdAt = Date(99999),
            content = Random.nextLong().toString()
        )

        // buffer messages
        runBlocking {
            cache.saveUnsentMessage(message1)
            cache.saveUnsentMessage(message2)
        }

        // flush messages
        runBlocking {
            messageRepository.flushPendingMessages(User(id = senderId)).collect {
                when (it) {
                    is ApiResponse.Failure -> fail("flushPendingMessages failed")
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> assertTrue(it.data)
                }
            }
        }

        var messages: List<Message>

        runBlocking { messages = cache.getUnsentMessagesOfUser(senderId) }

        assertEquals("Flushed messages should be removed from the buffer", 0, messages.size)

        // fetch flushed messages from the database
        runBlocking {
            launch {
                messageRepository.getConversationMessages(conversation)
                    .collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                // check there are only 2 messages
                                assertEquals(
                                    "Only 2 messages should have been flushed",
                                    2,
                                    it.data.size
                                )

                                // oldest messages should come first
                                assertEquals(
                                    "First message correctly retrieved",
                                    message1.content,
                                    it.data.first().content
                                )
                                assertEquals(
                                    "Second message correctly retrieved",
                                    message2.content,
                                    it.data.last().content
                                )

                                cancel()
                            }
                            is ApiResponse.Failure -> {
                                fail("Get Conversation Messages returned failure")
                                cancel()
                            }
                            is ApiResponse.Loading -> {}
                        }
                    }
            }
        }

        localDb.clearAllTables()
    }

    @Test
    fun messageListOfNewConversationIsEmpty() {
        val user1 = MockAuthRepositoryImpl.loggedInUser
        val user2Id = "fake-user-${Random.nextLong()}"
        val conversation = Conversation(user1Id = user1.id, user2Id = user2Id)
        lateinit var dbConversation: Conversation

        // create the conversation
        runBlocking {
            conversationRepository.createConversation(conversation).collect {
                when (it) {
                    is ApiResponse.Success -> dbConversation = it.data
                    else -> {}
                }
            }
        }

        // retrieve messages from the conversation
        runBlocking {
            launch {
                messageRepository.getConversationMessages(dbConversation).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            assert(it.data.isEmpty())
                            cancel()
                        }
                        is ApiResponse.Failure -> {
                            fail("Get Conversation Messages returned failure")
                            cancel()
                        }
                        is ApiResponse.Loading -> {}
                    }
                }
            }
        }
    }

    @Test
    fun sentMessagesAreCorrectlyRetrieved() {
        val user1 = MockAuthRepositoryImpl.loggedInUser
        val user2Id = "fake-user-${Random.nextLong()}"
        val conversation = Conversation(user1Id = user1.id, user2Id = user2Id)
        lateinit var dbConversation: Conversation

        // create the conversation
        runBlocking {
            conversationRepository.createConversation(conversation).collect {
                when (it) {
                    is ApiResponse.Success -> dbConversation = it.data
                    else -> {}
                }
            }
        }

        val message1Content = "Hello dude!"
        val message2Content = "I want your product!"
        val message1 = Message(
            conversationId = dbConversation.id,
            senderId = user1.id,
            content = message1Content
        )
        val message2 = Message(
            conversationId = dbConversation.id,
            senderId = user1.id,
            content = message2Content
        )

        // send message 1
        runBlocking {
            sendMessage(dbConversation, message1).collect {
                if (it is ApiResponse.Failure) fail("Message 1 could not be sent")
            }
        }

        // send message 2
        runBlocking {
            sendMessage(dbConversation, message2).collect {
                if (it is ApiResponse.Failure) fail("Message 2 could not be sent")
            }
        }

        // retrieve messages from the conversation
        runBlocking {
            launch {
                messageRepository.getConversationMessages(dbConversation).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            // check there are only 2 messages
                            assert(it.data.size == 2)

                            // oldest messages should come first
                            assert(it.data.first().content == message1Content)
                            assert(it.data.last().content == message2Content)

                            cancel()
                        }
                        is ApiResponse.Failure -> {
                            fail("Get Conversation Messages returned failure")
                            cancel()
                        }
                        is ApiResponse.Loading -> {}
                    }
                }
            }
        }
    }

    @Test
    fun usersGetReorderedSoThatUser1IsTheLoggedInUser() {
        val user1 = MockAuthRepositoryImpl.loggedInUser
        val user2Id = "fake-user-${Random.nextLong()}"
        lateinit var dbConversation: Conversation

        // user 2 initiates the conversation
        val conversation = Conversation(user1Id = user2Id, user2Id = user1.id)

        // create the conversation
        runBlocking {
            conversationRepository.createConversation(conversation).collect {
                when (it) {
                    is ApiResponse.Success -> dbConversation = it.data
                    else -> {}
                }
            }
        }

        // retrieve conversation
        runBlocking {
            conversationRepository.getConversation(user1, dbConversation.id).collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Failure -> fail("Get Conversation returned failure")
                    is ApiResponse.Success -> {
                        // assert users got re-ordered
                        assertEquals(it.data.user1Id, user1.id)
                        assertEquals(it.data.user2Id, user2Id)
                    }
                }
            }
        }
    }
}
