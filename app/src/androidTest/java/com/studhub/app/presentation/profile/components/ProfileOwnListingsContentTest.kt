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
        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = emptyList(),
                drafts = emptyList(),
                navigateToProfile = {},
                navigateToListing = {},
                navigateToDraft = {},
                isLoading = false
            )
        }

        composeTestRule
            .onNodeWithText(str(R.string.profile_own_listings_no_drafts))
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(str(R.string.profile_own_listings_no_listings))
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun listingsAreCorrectlyDisplayed() {
        val listing1 = Listing(name = "Listing A")
        val listing2 = Listing(name = "Listing B")
        val listing3 = Listing(name = "Listing C")
        val draft1 = Listing(name = "Draft A")
        val draft2 = Listing(name = "Draft B")
        val draft3 = Listing(name = "Draft C")
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

        val drafts: List<Listing> = listOf(
            draft1,
            draft2,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            dummyListing, dummyListing, dummyListing, dummyListing,
            draft3,
        )

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                drafts = drafts,
                navigateToProfile = {},
                navigateToListing = {},
                navigateToDraft = {},
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

        composeTestRule
            .onNodeWithText(draft1.name)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(draft2.name)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(draft3.name)
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
        val drafts = listOf(
            Listing(id = "d01234", name = "d01234"),
            Listing(id = expectedListingId, name = "DRAFT$listingToClickName"),
            Listing(id = "d56789", name = "d56789"),
        )

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                drafts = drafts,
                navigateToProfile = {},
                navigateToListing = { id -> clickedId = id },
                navigateToDraft = { id -> clickedId = "DRAFT$id"},
                isLoading = false
            )
        }

        composeTestRule
            .onNodeWithText(listingToClickName)
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertEquals("Should be able to click on listing", expectedListingId, clickedId)

        composeTestRule
            .onNodeWithText("DRAFT$listingToClickName")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertEquals("Should be able to click on draft", "DRAFT$expectedListingId", clickedId)
    }

    @Test
    fun clickingOnBackButtonWorksCorrectly() {
        val listings: List<Listing> = emptyList()
        var clicked = false

        composeTestRule.setContent {
            ProfileOwnListingsContent(
                listings = listings,
                drafts = emptyList(),
                navigateToProfile = { clicked = true },
                navigateToListing = {},
                navigateToDraft = {},
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
