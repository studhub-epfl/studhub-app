package com.studhub.app.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.ui.BasicFilledButton
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BasicFilledButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun componentBasicFilledButtonExistsWithCorrectGivenNameWhenCreated() {
        composeTestRule.setContent {
            BasicFilledButton(onClickHandler = { /*TODO*/ }, label = "Test")
        }

        composeTestRule.onNodeWithText("Test").assertExists()
    }

    @Test
    fun componentBasicFilledButtonCorrectlyInvokePassedMethodWhenClicked() {
        var callbackInvoked = false
        val callback: () -> Unit = { callbackInvoked = true }

        // Render the button composable with the callback method
        composeTestRule.setContent {
            BasicFilledButton(onClickHandler = { callback() }, label = "Test")
        }

        // Simulate a click on the button
        composeTestRule.onNodeWithText("Test").performClick()

        // Assert that the callback was invoked
        assertTrue(callbackInvoked)
    }
}