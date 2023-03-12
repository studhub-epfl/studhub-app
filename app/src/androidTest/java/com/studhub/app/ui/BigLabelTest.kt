package com.studhub.app.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.ui.BigLabel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BigLabelTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun componentBigLabelExistsWithCorrectGivenNameWhenCreated() {
        val name = "This is my super duper awesome name!"
        composeTestRule.setContent {
            BigLabel(label = name)
        }
        composeTestRule.onNodeWithText(name).assertExists()
    }
}