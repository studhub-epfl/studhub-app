package com.studhub.app.presentation.auth.verify.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VerifyEmailTopBarTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun verifyEmailTopBarDisplaysCorrectTitle() {
        composeTestRule.setContent {
            VerifyEmailTopBar(onSignOut = { })
        }

        composeTestRule.onNodeWithText(str(R.string.auth_verify_title)).assertIsDisplayed()
    }

    @Test
    fun signOutButtonCallsCorrectCallback() {
        var clicked = false
        composeTestRule.setContent {
            VerifyEmailTopBar(onSignOut = { clicked = true })
        }

        composeTestRule.onNodeWithText(str(R.string.auth_verify_btn_sign_out)).performClick()

        assertTrue("onSignOut callback should have been called", clicked)
    }
}
