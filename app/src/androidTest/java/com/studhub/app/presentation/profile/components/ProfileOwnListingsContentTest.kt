package com.studhub.app.presentation.profile.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.R
import com.studhub.app.domain.model.Listing
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProfileOwnListingsContentTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyListOfListingsDisplayCorrectMessage() {
        val listings: List<Listing> = emptyList()

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                navigateToProfile = {},
                navigateToListing = {},
                isLoading = false
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.profile_own_listings_no_listings))
            .assertIsDisplayed()
    }

    @Test
    fun listingsAreCorrectlyDisplayed() {
        val listing1 = Listing(name = "Listing A")
        val listing2 = Listing(name = "Listing B")
        val listing3 = Listing(name = "Listing C")
        val dummyListing = Listing(name = "Dummy")

        val listings: List<Listing> = listOf(
            listing1,
            listing2,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            listing3,
        )

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                navigateToProfile = {},
                navigateToListing = {},
                isLoading = false
            )
        }

        composeTestRule
            .onNodeWithText(listing1.name)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(listing2.name)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(listing3.name)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickingOnAListingWorksCorrectly() {
        var clickedId = ""
        val expectedListingId = "Listing-A"
        val listingToClickName = "Click me"
        val listings = listOf(
            Listing(id = "01234", name = "01234"),
            Listing(id = expectedListingId, name = listingToClickName),
            Listing(id = "56789", name = "56789"),
        )

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                navigateToProfile = {},
                navigateToListing = { id -> clickedId = id },
                isLoading = false
            )
        }

        composeTestRule
            .onNodeWithText(listingToClickName)
            .assertIsDisplayed()
            .performClick()

        assertEquals("Back to profile button was clicked", expectedListingId, clickedId)
    }

    @Test
    fun clickingOnBackButtonWorksCorrectly() {
        val listings: List<Listing> = emptyList()
        var clicked = false

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                navigateToProfile = { clicked = true },
                navigateToListing = {},
                isLoading = false
            )
        }

        composeTestRule
            .onNodeWithContentDescription(str(R.string.misc_btn_go_back))
            .assertIsDisplayed()
            .performClick()

        assertEquals("Back to profile button was clicked", true, clicked)
    }
}
