package com.studhub.app.presentation.conversation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Conversation
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun chatTopBarDisplaysCorrectContent() {
        val conversation = Conversation(user2Name = "Michel")
        var clicked = false
        composeTestRule.setContent {
            ChatTopBar(correspondentName = conversation.user2Name, navigateBack = { clicked = true })
        }

        composeTestRule.onNodeWithText(conversation.user2Name).assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Back", substring = true, ignoreCase = true)
            .assertIsDisplayed()
            .performClick()

        assertTrue(clicked)
    }
}
