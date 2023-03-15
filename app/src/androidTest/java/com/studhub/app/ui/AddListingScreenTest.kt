package com.studhub.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddListingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun AddListingScreenTest(){
        composeTestRule.setContent {
            AddListingScreen()
        }

        // Check if related text is displayed
        composeTestRule.onNodeWithText("List your item: ").assertIsDisplayed()
    }

}
