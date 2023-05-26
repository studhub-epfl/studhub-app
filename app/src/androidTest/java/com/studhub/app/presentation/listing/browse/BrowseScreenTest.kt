package com.studhub.app.presentation.listing.browse

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.studhub.app.data.repository.*
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import com.studhub.app.domain.usecase.listing.GetListing
import com.studhub.app.domain.usecase.listing.GetListings
import com.studhub.app.domain.usecase.listing.GetListingsBySearch
import com.studhub.app.domain.usecase.listing.PlaceBid
import com.studhub.app.domain.usecase.user.*
import com.studhub.app.presentation.listing.browse.BrowseScreen
import com.studhub.app.presentation.listing.browse.BrowseViewModel
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
class BrowseScreenTest {
    private val listingRepo = MockListingRepositoryImpl()
    private val authRepo = MockAuthRepositoryImpl()
    private val userRepo = MockUserRepositoryImpl()
    private val categoryRepo = MockCategoryRepositoryImpl()

    private val getListings = GetListings(listingRepo)
    private val getListingsBySearch = GetListingsBySearch(listingRepo,authRepo,userRepo)
    private val getCategories = GetCategories(categoryRepo)

    private val viewModel = BrowseViewModel(getListingsBySearch,getListings, getCategories)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    lateinit var listing: Listing

    @Before
    fun setup() {
        hiltRule.inject()

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())


        composeTestRule.setContent {
            BrowseScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }

    @Test
    fun detailedListingScreenMainCallElementsAreDisplayed() {
        composeTestRule.onNodeWithText("Search...").assertIsDisplayed()
        composeTestRule.onNodeWithText("MIN....CHF").assertIsDisplayed()
        composeTestRule.onNodeWithText("MAX....CHF").assertIsDisplayed()

    }


}



