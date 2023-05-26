package com.studhub.app.presentation.auth.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInContentTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun signInContentDisplaysCorrectElements() {
        composeTestRule.setContent {
            SignInContent(
                padding = PaddingValues(),
                signIn = { _, _ -> },
                navigateToForgotPasswordScreen = { },
                navigateToSignUpScreen = { }
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.auth_signin_welcome_message))
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun signInTransmitsCorrectCredentials() {
        var receivedEmail = ""
        var receivedPasswd = ""

        composeTestRule.setContent {
            SignInContent(
                padding = PaddingValues(),
                signIn = { email, password ->
                    receivedEmail = email
                    receivedPasswd = password
                },
                navigateToForgotPasswordScreen = { },
                navigateToSignUpScreen = { }
            )
        }

        val email = "e.m@a.il"
        val pwd = "1243"

        composeTestRule
            .onNodeWithText("Email")
            // .performScrollTo()
            .assertIsDisplayed()
            .performTextInput(email)

        composeTestRule
            .onNodeWithText("Password")
            // .performScrollTo()
            .assertIsDisplayed()
            .performTextInput(pwd)

        composeTestRule
            .onNodeWithText(str(R.string.auth_signin_btn_submit))
            // .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertEquals("Received email should be the typed one", email, receivedEmail)
        assertEquals("Received password should be the typed one", pwd, receivedPasswd)
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun clickForgotPasswordCallsGivenCallback() {
        var clicked = false

        composeTestRule.setContent {
            SignInContent(
                padding = PaddingValues(),
                signIn = { _, _ -> },
                navigateToForgotPasswordScreen = { clicked = true },
                navigateToSignUpScreen = { }
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.auth_signin_btn_forgot_password))
            .assertIsDisplayed()
            .performClick()

        assertTrue("navigateToForgotPasswordScreen callback should have been called", clicked)
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun clickNoAccountCallsGivenCallback() {
        var clicked = false

        composeTestRule.setContent {
            SignInContent(
                padding = PaddingValues(),
                signIn = { _, _ -> },
                navigateToForgotPasswordScreen = { },
                navigateToSignUpScreen = { clicked = true }
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.auth_signin_btn_no_account))
            .assertIsDisplayed()
            .performClick()

        assertTrue("navigateToSignUpScreen callback should have been called", clicked)
    }
}
