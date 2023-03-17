package com.studhub.app.presentation.ui.browse

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListingThumbnailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun listingThumbnailDisplaysCorrectly() {
        val listing = Listing(
            name = "Scooter brand new from 2021",
            seller = User(firstName = "Jimmy", lastName = "Poppin"),
            categories = listOf(Category(name = "Mobility")),
            price = 1560.45F
        )

        composeTestRule.setContent {
            ListingThumbnail(listing = listing)
        }

        // Verify that the listing name is displayed
        composeTestRule.onNodeWithText("Scooter brand new from 2021").assertIsDisplayed()

        // Verify that the listing price is displayed
        composeTestRule.onNodeWithText("1560.45 chf").assertIsDisplayed()

        // Verify that the category name is displayed
        composeTestRule.onNodeWithText("Category: Mobility").assertIsDisplayed()

        // Verify that the seller name is displayed
        composeTestRule.onNodeWithText("Sold by: Jimmy Poppin").assertIsDisplayed()
    }
}
