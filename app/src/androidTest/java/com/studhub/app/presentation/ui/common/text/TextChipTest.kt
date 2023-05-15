package com.studhub.app.presentation.ui.common.text

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
class TextChipTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textChip_isDisplayed() {
        val label = "Test Label"

        composeTestRule.setContent {
            TextChip(label = label)
        }

        composeTestRule.onNodeWithText(label).assertExists()
    }

    @Test
    fun textChip_onClick_isCalled() {
        var clicked = false
        val label = "Test Label"

        composeTestRule.setContent {
            TextChip(label = label, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText(label).performClick()

        assertTrue(clicked)
    }

    @Test
    fun textChip_showsTrailingIcon_whenProvided() {
        val label = "Test Label"

        composeTestRule.setContent {
            TextChip(label = label, trailingIcon = { Icon(Icons.Filled.Add, contentDescription = "add icon") })
        }

        composeTestRule.onNodeWithContentDescription("add icon").assertExists()
    }
}
