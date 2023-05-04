package com.studhub.app.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.presentation.listing.browse.components.SearchBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSearchBar() {
        composeTestRule.setContent {
            SearchBar()
        }
        /*

        // Check that the menu button is displayed and clickable
        composeTestRule.onNodeWithContentDescription("Menu button").assertIsDisplayed().performClick()

        // Check that the search bar is displayed and has the correct label
        composeTestRule.onNodeWithText("Search...").assertIsDisplayed()

        // Type a query into the search bar
        composeTestRule.onNodeWithText("Search...").performTextInput("Test query")
        composeTestRule.onNodeWithText("Test query").assertIsDisplayed().assertIsFocused()

        // Clear the query by clicking the clear button
        composeTestRule.onNodeWithContentDescription("Clear button").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("").assertIsDisplayed().assertIsFocused()
        */

    }
}
