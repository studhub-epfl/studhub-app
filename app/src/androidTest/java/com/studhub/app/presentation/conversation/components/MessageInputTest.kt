package com.studhub.app.presentation.conversation.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Conversation
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MessageInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun messageInputSendsTypedInText() {
        val textToWrite = "Hello how are you??"
        var text = ""

        composeTestRule.setContent {
            MessageInput(onSend = { text = it })
        }

        composeTestRule
            .onNodeWithText("Write", substring = true, ignoreCase = true)
            .assertIsDisplayed()
            .performTextInput(textToWrite)

        composeTestRule
            .onNodeWithContentDescription("Send", substring = true, ignoreCase = true)
            .assertIsDisplayed()
            .performClick()

        assertEquals(text, textToWrite)
    }
}
