package com.studhub.app.presentation.ui.common.text

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

    @Before
    fun setUp() {
        composeTestRule.setContent {
            TextChip(label = name)
        }
    }

    @Test
    fun componentTextChipIsCreatedWithCorrectName() {
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
    }
}
