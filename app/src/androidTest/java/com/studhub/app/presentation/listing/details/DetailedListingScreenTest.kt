package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
            val isFavorite = remember { mutableStateOf(true) }
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
                onContactSellerClick = { },
                onFavoriteClicked = { isFavorite.value = !isFavorite.value },
                isFavorite = isFavorite.value
            )
        }
    }

    @Test
    fun detailsListingScreenDisplaysAllElements() {
        composeTestRule.onNodeWithText("Contact seller").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Remove from favorites").performClick()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.description).assertIsDisplayed()
        composeTestRule.onNodeWithTag("price")
    }
}
