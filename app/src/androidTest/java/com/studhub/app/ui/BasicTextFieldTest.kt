package com.studhub.app.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.ui.BasicTextField
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BasicTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @JvmField
    var rememberable: MutableState<String>? = null

    @Test
    fun componentBasicTextFieldGetsCreatedWithCorrectName() {
        composeTestRule.setContent {
            BasicTextField("This is my super name!")
        }
        composeTestRule.onNodeWithText("This is my super name!").assertExists()
    }

    @Test
    fun componentBasicTextFieldWritesUpdateTheField() {
        composeTestRule.setContent {
            BasicTextField("Test")
        }
        val field = composeTestRule.onNodeWithText("Test")
        val text = "Super test I hope I work !àéè+4èü"
        field.performTextInput(text)
        field.assert(hasText(text))
    }

    @Test
    fun componentBasicTextFieldUpdatesPassedRememberableCorrectly() {
        composeTestRule.setContent {
            rememberable = rememberSaveable { mutableStateOf("") }
            BasicTextField(label = "Test", rememberedValue = rememberable!!)
        }
        val field = composeTestRule.onNodeWithText("Test")
        field.performTextInput("Super test I hope I work !àéè+4èü")
        field.assert(hasText(rememberable!!.value))
    }
}