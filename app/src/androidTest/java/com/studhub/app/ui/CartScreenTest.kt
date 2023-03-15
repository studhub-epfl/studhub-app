package com.studhub.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun CartScreenTest(){
        composeTestRule.setContent {
            CartScreen()
        }

        // Check if related text is displayed
        composeTestRule.onNodeWithText("Your Cart:").assertIsDisplayed()
    }

}
