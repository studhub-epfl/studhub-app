package com.studhub.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun AboutScreenTest(){
        composeTestRule.setContent {
            AboutScreen()
        }

        // Check if related text is displayed
        composeTestRule.onNodeWithText("About Us:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contact us at ...").assertIsDisplayed()
        composeTestRule.onNodeWithText("We are a MarketPlace company working for EPFL campus.").assertIsDisplayed()
    }

}
