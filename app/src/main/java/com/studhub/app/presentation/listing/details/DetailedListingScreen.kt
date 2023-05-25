package com.studhub.app.presentation.listing.details

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.MeetingPointPickerActivity
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.core.utils.Utils
import com.studhub.app.core.utils.Utils.Companion.displayMessage
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.details.components.*
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.theme.StudHubTheme


@Composable
fun DetailedListingScreen(
    viewModel: IDetailedListingViewModel = hiltViewModel<DetailedListingViewModel>(),
    navigateToConversation: (conversationId: String) -> Unit,
    navigateToRateUser: (userId: String) -> Unit,
    id: String?
) {
    val bid = rememberSaveable { mutableStateOf("") }
    LaunchedEffect(id) {
        if (id != null)
            viewModel.fetchListing(id)
    }

    val activityContext = LocalContext.current

    fun displayMeetingPoint(location: LatLng) {
        val intent = Intent(activityContext, MeetingPointPickerActivity::class.java).apply {
            putExtra("viewOnly", true)
            putExtra("latitude", location.latitude)
            putExtra("longitude", location.longitude)
        }
        activityContext.startActivity(intent)
    }

    when (val currentListing = viewModel.currentListing) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            val listing = currentListing.data
            val isFavorite = viewModel.isFavorite.value
            val isBlocked = viewModel.isBlocked.value
            bid.value = String.format("%.2f", listing.price)
            val hasBid = listing.currentBidderId.equals(viewModel.userId)
            Details(
                listing = listing,
                onFavoriteClicked = { viewModel.onFavoriteClicked() },
                isFavorite = isFavorite,
                isBlocked = isBlocked,
                onContactSellerClick = {
                    viewModel.contactSeller(listing.seller) { conv ->
                        navigateToConversation(conv.id)
                    }
                },
                onBlockedClicked = { viewModel.onBlockedClicked() },
                onMeetingPointClick = {
                    val meetingPoint = listing.meetingPoint
                    if (meetingPoint != null) {
                        displayMeetingPoint(LatLng(meetingPoint.latitude, meetingPoint.longitude))
                    }
                },
                onRateUserClick = { navigateToRateUser(listing.seller.id) },
                onBidPlaced = {
                    viewModel.placeBid(
                        bid = bid.value.toFloatOrNull(),
                        onError = { msg -> displayMessage(activityContext, msg) }
                    )
                },
                bid = bid,
                hasBid = hasBid
            )
        }
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun DetailsListingScreenBidPreview() {
    val listing = Listing(
        name = "Large white wooden desk",
        description = "This is the perfect desk for a home workplace",
        categories = listOf(Category(name = "Furniture")),
        seller = User(
            userName = "SuperChad",
            firstName = "Josh",
            lastName = "Marley",
        ),
        type = ListingType.BIDDING,
        price = 545.45F
    )

    StudHubTheme {
        Details(
            listing = listing,
            onContactSellerClick = {},
            onFavoriteClicked = {},
            isFavorite = true,
            onMeetingPointClick = {},
            onRateUserClick = {},
            onBlockedClicked = {},
            onBidPlaced = {},
            isBlocked = true,
            bid = rememberSaveable { mutableStateOf("545.45") },
            hasBid = false
        )
    }
}
