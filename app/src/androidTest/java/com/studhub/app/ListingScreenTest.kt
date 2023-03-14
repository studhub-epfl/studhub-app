package com.studhub.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.studhub.app.MainActivity
import com.studhub.app.domain.model.Listing
import org.junit.Rule
import org.junit.Test

class ListingScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun listingScreen_displaysListingDetails() {
        // Prepare the test data
        val testListing = Listing(
            id = 1,
            name = "Test Listing",
            description = "This is a test listing.",
            price = 100.0F
        )

        // Set up the UI content
        composeTestRule.setContent {
            ListingScreen(listing = testListing)
        }

        // Assertions
        composeTestRule.onNodeWithText("Test Listing").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test listing.").assertIsDisplayed()
        composeTestRule.onNodeWithText("$100.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contact seller").assertIsDisplayed()
        composeTestRule.onNodeWithText("Favourite").assertIsDisplayed()
    }
}
