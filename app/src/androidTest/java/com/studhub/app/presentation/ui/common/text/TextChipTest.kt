package com.studhub.app.presentation.ui.common.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextChipTest {

    val name = "This is my super duper awesome name!"

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun componentTextChipIsCreatedWithCorrectName() {
        composeTestRule.setContent {
            TextChip(label = name)
        }
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
    }

    @Test
    fun componentTextChipOnClickIsCorrectlyTriggered() {
        var clicked = false
        composeTestRule.setContent {
            TextChip(label = name, onClick = {clicked = true})
        }
        composeTestRule.onNodeWithText(name).performClick()
        assert(clicked)
    }

    @Test
    fun componentTextChipTrailingIconIsDisplayed() {
        composeTestRule.setContent {
            TextChip(
                label = "",
                trailingIcon = {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Test",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Test").assertIsDisplayed()
    }
}
