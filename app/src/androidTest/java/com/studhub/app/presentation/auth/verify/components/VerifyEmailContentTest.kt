package com.studhub.app.presentation.auth.verify.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VerifyEmailContentTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun verifyEmailContentDisplaysCorrectTexts() {
        composeTestRule.setContent {
            VerifyEmailContent(padding = PaddingValues(), reloadUser = { })
        }

        composeTestRule.onNodeWithText(str(R.string.auth_verify_check_spam)).assertIsDisplayed()
    }

    @Test
    fun verifiedButtonCallsCorrectCallback() {
        var clicked = false
        composeTestRule.setContent {
            VerifyEmailContent(padding = PaddingValues(), reloadUser = { clicked = true })
        }

        composeTestRule.onNodeWithText(str(R.string.auth_verify_btn_verified)).performClick()

        Assert.assertTrue("reloadUser callback should have been called", clicked)
    }
}
