package com.studhub.app.presentation.listing.details

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.data.repository.MockConversationRepositoryImpl
import com.studhub.app.data.repository.MockListingRepositoryImpl
import com.studhub.app.data.repository.MockUserRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.listing.PlaceBid
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailedListingScreenMainComposableTest {
    private val listingRepo = MockListingRepositoryImpl()
    private val authRepo = MockAuthRepositoryImpl()
    private val userRepo = MockUserRepositoryImpl()
    private val convoRepo = MockConversationRepositoryImpl()

    private val getListing = GetListing(listingRepo)
    private val getFavoriteListings = GetFavoriteListings(userRepo, authRepo)
    private val addFavoriteListing = AddFavoriteListing(userRepo, authRepo)
    private val removeFavoriteListing = RemoveFavoriteListing(userRepo, authRepo)
    private val startConversationWith = StartConversationWith(convoRepo, authRepo)
    private val addBlockedUser = AddBlockedUser(userRepo, authRepo)
    private val unblockUser = UnblockUser(userRepo, authRepo)
    private val getBlockedUsers = GetBlockedUsers(userRepo, authRepo)
    private val auth = authRepo
    private val placeBid = PlaceBid(listingRepo, authRepo)

    private val viewModel = DetailedListingViewModel(
        getListing,
        getFavoriteListings,
        addFavoriteListing,
        removeFavoriteListing,
        startConversationWith,
        addBlockedUser,
        unblockUser,
        getBlockedUsers,
        auth,
        placeBid
    )

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    lateinit var listing: Listing

    @Before
    fun setup() {
        hiltRule.inject()
        val listingId = "1234" // Save the listing ID

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

        runBlocking {
            listingRepo.createListing(listing).collect()
            delay(100)
        }

        composeTestRule.setContent {
            DetailedListingScreen(
                viewModel = viewModel,
                navigateToConversation = {},
                navigateToRateUser = {},
                id = listing.id
            )
        }
    }

    @Test
    fun detailedListingScreenMainCallElementsAreDisplayed() {
        composeTestRule.onNodeWithText("Contact seller").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(listing.description).assertIsDisplayed()
        composeTestRule.onNodeWithTag("price").assertIsDisplayed()
        composeTestRule.onNodeWithText("View Meeting Point").assertIsDisplayed()
    }

    @Test fun detailedListingScreenMainCallAddFavouriteDisplaysCorrectElement() {
        composeTestRule.onNodeWithContentDescription("Add to favorites").performClick()
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }

}
