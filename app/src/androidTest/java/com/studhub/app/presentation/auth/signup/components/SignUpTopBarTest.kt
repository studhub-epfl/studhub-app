package com.studhub.app.presentation.auth.signup.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import com.studhub.app.presentation.auth.forgot.components.ForgotPasswordTopBar
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpTopBarTest {
    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun signUpTopBarDisplaysCorrectTitle() {
        composeTestRule.setContent {
            SignUpTopBar(navigateBack = { })
        }

        composeTestRule.onNodeWithText(str(R.string.auth_signup_title)).assertIsDisplayed()
    }

    @Test
    fun backButtonCallsCorrectCallback() {
        var clicked = false
        composeTestRule.setContent {
            SignUpTopBar(navigateBack = { clicked = true })
        }

        composeTestRule.onNodeWithContentDescription(str(R.string.misc_btn_go_back)).performClick()

        Assert.assertTrue("navigateBack callback should have been called", clicked)
    }
}
