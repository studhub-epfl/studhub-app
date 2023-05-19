package com.studhub.app.presentation.listing.browse

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.model.User
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.studhub.app.R


@RunWith(AndroidJUnit4::class)
class ListingThumbnailScreenTest {

    private fun str(id: Int) =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    val listing = Listing(
        name = "Limited edition manga",
        seller = User(firstName = "Jimmy", lastName = "Poppin"),
        categories = listOf(Category(name = "Books")),
        type = ListingType.BIDDING,
        price = 80F
    )

    val viewModel = ListingThumbnailViewModel(listing)


    @get:Rule
    val composeTestRule = createComposeRule()

    //TODO needs more
    @Test
    fun listingThumbnailScreenChildrenAreDisplayed() {

        composeTestRule.setContent {
            ListingThumbnailScreen(viewModel = viewModel, onClick = {})
        }
        composeTestRule.onNodeWithText(str(R.string.bidding_type_indicator))
    }
}
