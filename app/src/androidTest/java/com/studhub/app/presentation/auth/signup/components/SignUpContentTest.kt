package com.studhub.app.presentation.auth.signup.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpContentTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun signUpContentDisplaysCorrectTexts() {
        composeTestRule.setContent {
            SignUpContent(
                padding = PaddingValues(),
                signUp = { _, _ -> },
                navigateBack = { },
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.auth_signup_info_title))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(str(R.string.auth_signup_info_description))
            .assertIsDisplayed()

    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun forgotPasswordTransmitsCorrectEmail() {
        var receivedEmail = ""
        var receivedPasswd = ""

        composeTestRule.setContent {
            SignUpContent(
                padding = PaddingValues(),
                signUp = { email, password ->
                    receivedEmail = email
                    receivedPasswd = password
                },
                navigateBack = { },
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
            .onNodeWithText(str(R.string.auth_signup_btn_submit))
            // .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        Assert.assertEquals("Received email should be the typed one", email, receivedEmail)
        Assert.assertEquals("Received password should be the typed one", pwd, receivedPasswd)
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun alreadyHaveAccountButtonCallsCorrectCallback() {
        var clicked = false

        composeTestRule.setContent {
            SignUpContent(
                padding = PaddingValues(),
                signUp = { _, _ -> },
                navigateBack = { clicked = true },
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.auth_signup_already_have_account))
            .assertIsDisplayed()
            .performClick()

        Assert.assertTrue(
            "navigateBack callback should have been called",
            clicked
        )
    }
}
