package com.studhub.app.presentation.conversation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class MessageListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun messageListDisplaysMessageCorrectly() {
        val messages = listOf(Message(content = "Hello my friend, my code is ${Random.nextFloat()}"))
        composeTestRule.setContent {
            MessageList(conversation = Conversation(), messages = messages)
        }

        composeTestRule.onNodeWithText(messages.first().content).assertIsDisplayed()
    }
}