package com.studhub.app.presentation.ui.common.button

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlusButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ComponentPlusButtonExistsWithCorrectGivenNameWhenCreated() {
        composeTestRule.setContent {
            PlusButton(onClick = { })
        }

        composeTestRule.onNodeWithContentDescription("Plus Icon").assertExists()
    }

    @Test
    fun componentPlusButtonCorrectlyInvokePassedMethodWhenClicked() {
        var callbackInvoked = false
        val callback: () -> Unit = { callbackInvoked = true }

        composeTestRule.setContent {
            PlusButton(onClick = { callback() })
        }

        composeTestRule.onNodeWithContentDescription("Plus Icon").performClick()

        TestCase.assertTrue(callbackInvoked)
    }
}
