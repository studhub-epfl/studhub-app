package com.studhub.app.presentation.conversation.components

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Conversation
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConversationItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun conversationItemDisplaysCorrectElements() {
        val date = Date()
        val conversation =
            Conversation(user2Name = "Bob", lastMessageContent = "Hello boys!", updatedAt = date)

        composeTestRule.setContent {
            ConversationItem(conversation = conversation)
        }

        composeTestRule.onNodeWithText(conversation.lastMessageContent).assertIsDisplayed()
        composeTestRule.onNodeWithText(conversation.user2Name).assertIsDisplayed()
    }

    @Test
    fun conversationItemHidesInfoWhenLastMessageContentIsEmpty() {
        val date = Date()
        val conversation =
            Conversation(user2Name = "Bob", updatedAt = date)

        composeTestRule.setContent {
            ConversationItem(conversation = conversation)
        }

        var assertFailed = false

        try {
            composeTestRule.onNodeWithText(formatTime(date), substring = true).assertIsDisplayed()
        } catch (e: AssertionError) {
            assertFailed = true
        }

        assertTrue(assertFailed)
    }

    @Test
    fun conversationItemShortenTextWhenTooLong() {
        val longText =
            "Hello boys! Hello boys! Hello boys! Hello boys! Hello boys! Hello boys! Hello boys! Hello boys! Hello boys! Hello boys! Hello boys!"
        val conversation =
            Conversation(
                user2Name = "Bob",
                lastMessageContent = longText
            )

        composeTestRule.setContent {
            ConversationItem(conversation = conversation)
        }

        composeTestRule.onNodeWithText(shortenText(longText, ellipsis = "…"))
    }

    private fun formatTime(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return formatter.format(calendar.time)
    }

    private fun shortenText(text: String, limit: Int = 40, ellipsis: String = ""): String {
        return text.take(limit) + if (text.length > limit) ellipsis else ""
    }

}