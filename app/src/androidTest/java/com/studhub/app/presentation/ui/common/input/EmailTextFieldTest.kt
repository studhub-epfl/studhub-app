package com.studhub.app.presentation.ui.common.input

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmailTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun componentEmailTextFieldGetsCreatedWithCorrectName() {
        composeTestRule.setContent {
            EmailTextField("EMAIL", email = TextFieldValue(), onEmailValueChange = {})
        }
        composeTestRule.onNodeWithText("EMAIL").assertExists()
    }

    @Test
    fun componentEmailTextFieldWritesUpdateTheField() {
        lateinit var rememberable: TextFieldValue
        composeTestRule.setContent {
            rememberable = rememberSaveable { TextFieldValue("") }
            EmailTextField(
                label = "Test",
                email = rememberable,
                onEmailValueChange = { rememberable = it })
        }
        val field = composeTestRule.onNodeWithText("Test")
        val text = "e@m.ail"
        field.performTextInput(text)
        field.assert(hasText(text))
    }

    @Test
    fun componentEmailTextFieldUpdatesPassedRememberableCorrectly() {
        lateinit var rememberable: TextFieldValue
        composeTestRule.setContent {
            rememberable = rememberSaveable { TextFieldValue("") }
            EmailTextField(
                label = "Test",
                email = rememberable,
                onEmailValueChange = { rememberable = it })
        }
        val field = composeTestRule.onNodeWithText("Test")
        field.performTextInput("e@m.ail")
        field.assert(hasText(rememberable.text))
    }
}
