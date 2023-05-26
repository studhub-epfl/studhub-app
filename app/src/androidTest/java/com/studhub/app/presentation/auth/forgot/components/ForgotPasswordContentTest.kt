package com.studhub.app.presentation.auth.forgot.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgotPasswordContentTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun forgotPasswordTransmitsCorrectEmail() {
        var receivedEmail = ""

        composeTestRule.setContent {
            ForgotPasswordContent(
                padding = PaddingValues(),
                sendPasswordResetEmail = { receivedEmail = it }
            )
        }

        val email = "e.m@a.il"

        composeTestRule
            .onNodeWithText("Email")
            // .performScrollTo()
            .assertIsDisplayed()
            .performTextInput(email)

        composeTestRule
            .onNodeWithText(str(R.string.auth_forgot_btn_submit))
            // .performScrollTo()
            .assertIsDisplayed()
            .performClick()


        assertEquals("Received email should be the typed one", email, receivedEmail)
    }
}
