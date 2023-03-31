package com.studhub.app.presentation.listing.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailedListingScreenTest {

    lateinit var listing: Listing

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            listing = Listing(
                name = "Large white wooden desk",
                description = "This is the perfect desk for a home workplace",
                categories = listOf(Category(name = "Furniture")),
                seller = User(
                    userName = "SuperChad",
                    firstName = "Josh",
                    lastName = "Marley",
                ),
                price = 545.45F
            )
            Details(
                listing = listing,
                onContactSellerClick = { }
            )
        }
    }

    @Test
    fun detailsListingScreenDisplaysAllElements() {
        composeTestRule.onNodeWithText("Contact seller").assertIsDisplayed()
        composeTestRule.onNodeWithText("Favourite").assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.name).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Item picture").assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.description).assertIsDisplayed()
        composeTestRule.onNodeWithTag("price")
    }
}
