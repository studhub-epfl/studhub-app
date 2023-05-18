package com.studhub.app.presentation.listing.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.wrapper.DetailedListingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailedListingScreenTest {

    lateinit var listing: Listing
    lateinit var isFavorite: MutableState<Boolean> // Define as top-level property
    lateinit var isBlocked: MutableState<Boolean> // Define as top-level property

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<DetailedListingActivity>()


    @Inject
    lateinit var viewModel: IDetailedListingViewModel


    @Before
    fun setup() {
        hiltRule.inject()
        isFavorite = mutableStateOf(true) // Initialize here
        isBlocked = mutableStateOf(true) // Initialize here
        val listingId = "hier" // Save the listing ID

        listing = Listing(
            id = listingId,
            name = "Large white wooden desk",
            description = "This is the perfect desk for a home workplace",
            categories = listOf(Category(name = "Furniture")),
            seller = User(
                userName = "SuperChad",
                firstName = "Josh",
                lastName = "Marley",
            ),
            price = 545.45F,

            )
        composeTestRule.setContent {
            Details(
                listing = listing,
                onContactSellerClick = { },
                onMeetingPointClick = { },
                onFavoriteClicked = { isFavorite.value = !isFavorite.value },
                isFavorite = isFavorite.value,
                onRateUserClick = { },
                isBlocked = isBlocked.value,
                onBlockedClicked = { isBlocked.value = !isBlocked.value }
            )
        }
    }
    @Test
    fun detailsListingScreenDisplaysAllElements() {
        composeTestRule.onNodeWithText("Contact seller").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Remove from favorites").performClick()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Unblock User").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Unblock User").performClick()
        composeTestRule.onNodeWithContentDescription("Block User").assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.description).assertIsDisplayed()
        composeTestRule.onNodeWithTag("price")
    }

    @Test
    fun detailsListingScreenDisplaysContactSellerButton() {
        composeTestRule.onNodeWithText("Contact seller").assertIsDisplayed()
    }

    @Test
    fun detailsListingScreenDisplaysFavoriteButton() {
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Remove from favorites").performClick()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
    }

    @Test
    fun detailsListingScreenDisplaysListingName() {
        composeTestRule.onNodeWithText(listing.name).assertIsDisplayed()
    }

    @Test
    fun detailsListingScreenDisplaysListingDescription() {
        composeTestRule.onNodeWithText(listing.description).assertIsDisplayed()
    }

    @Test
    fun detailsListingScreenDisplaysListingPrice() {
        composeTestRule.onNodeWithTag("price").assertIsDisplayed()
    }

    @Test
    fun detailsListingScreenDisplaysMeetingPointButton() {
        composeTestRule.onNodeWithText("View Meeting Point").assertIsDisplayed()
    }


}
