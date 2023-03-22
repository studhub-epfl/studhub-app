package com.studhub.app.presentation.listing.add

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.add.ListingScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testListingScreen() {
        val mockOnContactSellerClick: () -> Unit = {}
        val mockOnFavouriteClick: () -> Unit = {}

        val listing = Listing(
            id = "1",
            name = "iPhone 13",
            description = "The latest iPhone with a 6.1-inch Super Retina XDR display.",
            seller = User(),
            price = 999.99F,
            categories = listOf(
                Category(name = "Electronics"),
                Category(name = "Smartphones")
            )
        )

        composeTestRule.setContent {
            ListingScreen(
                listing = listing,
                onContactSellerClick = mockOnContactSellerClick,
                onFavouriteClick = mockOnFavouriteClick
            )
        }
        // wait for UI
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(listing.name).assertIsDisplayed()
        composeTestRule.onNodeWithText("${listing.price} CHF").assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.description).assertIsDisplayed()
    }
}
