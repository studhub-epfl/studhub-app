package com.studhub.app.presentation.conversation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.ui.chat.ChatViewModel
import com.studhub.app.wrapper.ChatActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChatScreenTest {

    private val conversation = Conversation(
        id = "1",
        user1Id = "user1",
        user2Id = "user2",
        lastMessageContent = "Hi there!"
    )

    private val testMessages = listOf(
        Message(
            id = "1",
            conversationId = conversation.id,
            senderId = conversation.user1Id,
            content = "Hi there!",
            createdAt = Date(1000)
        ),
        Message(
            id = "2",
            conversationId = conversation.id,
            senderId = conversation.user2Id,
            content = "Hello!",
            createdAt = Date(2000)
        )
    )

    private val viewModel = mockk<ChatViewModel>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ChatActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        coEvery { viewModel.loadMessages(conversation) } returns Unit
        coEvery { viewModel.sendMessage(conversation, any()) } returns Unit
        coEvery { viewModel.messages } returns MutableStateFlow(testMessages)
        composeTestRule.setContent {
            ChatScreen(viewModel, conversation)
        }
    }

    @Test
    fun messageList_displaysMessages() {
        testMessages.forEach { message ->
            composeTestRule
                .onNodeWithText(message.content, useUnmergedTree = true)
                .assertExists()
        }
    }

    @Test
    fun messageInput_sendsMessage() {
        val messageText = "Hello!"
        val textField = composeTestRule.onNodeWithContentDescription("Message")
        val sendButton = composeTestRule.onNodeWithText("Send")

        // Enter text in the text field
        textField.performTextInput(messageText)
        // Click the send button
        sendButton.performClick()

        // Check that the view model's sendMessage method was called with the expected message
        coEvery { viewModel.sendMessage(conversation, Message(content = messageText)) } returns Unit
    }

}
