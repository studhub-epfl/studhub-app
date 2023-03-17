package com.studhub.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.home.HomeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreen() {
        val mockOnAddListingClick: () -> Unit = {}
        val mockOnBrowseClick: () -> Unit = {}
        val mockOnCartClick: () -> Unit = {}
        val mockOnAboutClick: () -> Unit = {}

        composeTestRule.setContent {
            HomeScreen(
                onAddListingClick = mockOnAddListingClick,
                onBrowseClick = mockOnBrowseClick,
                onCartClick = mockOnCartClick,
                onAboutClick = mockOnAboutClick,
            )
        }


        // Check if "Welcome to our app!" text is displayed
        composeTestRule.onNodeWithText("Welcome to our app!").assertIsDisplayed()

        // Check if "//FILL" text is displayed
        composeTestRule.onNodeWithText("//FILL").assertIsDisplayed()

        // Check if "Add Listing" button is displayed
        composeTestRule.onNodeWithText("Add Listing").assertIsDisplayed()

        // Check if "Browse" button is displayed
        composeTestRule.onNodeWithText("Browse").assertIsDisplayed()

        // Check if "Cart" button is displayed
        composeTestRule.onNodeWithText("Cart").assertIsDisplayed()

        // Check if "About" button is displayed
        composeTestRule.onNodeWithText("About").assertIsDisplayed()
    }
}
