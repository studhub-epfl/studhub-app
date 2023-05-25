package com.studhub.app.presentation.listing.details

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.data.repository.MockConversationRepositoryImpl
import com.studhub.app.data.repository.MockListingRepositoryImpl
import com.studhub.app.data.repository.MockUserRepositoryImpl
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailedListingViewModelTest {
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

    val viewModel = DetailedListingViewModel(
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

    private val seller = User(id = "blocked1234", userName = "seller")
    val listing =
        Listing(id = "test1234", name = "test", sellerId = seller.id, seller = seller, price = 20f)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()

        userRepo.listingDB[listing.id] = listing

        runBlocking {
            listingRepo.createListing(listing).collect()
        }

        runBlocking {
            viewModel.fetchListing(listing.id)
        }

        runBlocking {
            userRepo.addFavoriteListing(authRepo.currentUserUid, listing).collect()
        }
        runBlocking { delay(100) }
    }

    @Test
    fun detailedListingViewModelFetchListingGetsCorrectListing() {

        assertEquals(
            "ViewModel's current listing should match the given listing",
            listing.name,
            (viewModel.currentListing as ApiResponse.Success).data.name
        )
    }

    @Test
    fun detailedListingViewModelGetIsFavoriteIsCorrect() {

        runBlocking {
            viewModel.getIsFavorite()
            delay(100)
        }

        assert(viewModel.isFavorite.value)
    }

    @Test
    fun detailedListingViewModelPlaceBidUpdatesPrice() {

        userRepo.userDB[auth.currentUserUid]!!.blockedUsers = mapOf()


        runBlocking {
            viewModel.placeBid(bid = 40f, onError = {})
            delay(100)
        }

        var l: Listing
        runBlocking {
            l = (listingRepo.getListing(listing.id).first() as ApiResponse.Success).data
            delay(100)
        }

        assertEquals("Price should match", 40f, l.price)
    }

    @Test
    fun detailedListingViewModelGetIsBlockedIsCorrect() {

        userRepo.userDB[listing.sellerId] = seller
        userRepo.userDB[auth.currentUserUid]!!.blockedUsers = mapOf(listing.sellerId to true)

        runBlocking {
            viewModel.getIsBlocked()
            delay(100)
        }

        assert(viewModel.isBlocked.value)
    }

    @Test
    fun detailedListingViewModelOnFavouriteClickedChangesFavouriteStatus() {
        runBlocking {
            viewModel.onFavoriteClicked()
            delay(1000)
        }

        //the listing is already in favourite so we click it off
        assert(!viewModel.isFavorite.value)

        //then we click it back on
        runBlocking {
            viewModel.onFavoriteClicked()
            delay(1000)
        }


        assert(viewModel.isFavorite.value)
    }

    @Test
    fun detailedListingViewModelOnBlockClickedBlocksTheListing() {
        runBlocking {
            viewModel.onBlockedClicked()
            delay(100)
        }

        //listing blocked
        assert(viewModel.isBlocked.value)

        //now we click again to see if it unblocks
        runBlocking {
            viewModel.onBlockedClicked()
            delay(100)
        }

        assert(!viewModel.isBlocked.value)
    }

    @Test
    fun detailedListingViewModelContactSellerSucceeds() {
        var callback = false
        runBlocking {
            viewModel.contactSeller(seller = seller, callback = { callback = true })
            delay(100)
        }
        assert(callback)
    }
}
