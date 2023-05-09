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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.MeetingPointPickerActivity
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.details.components.FavoriteButton
import com.studhub.app.presentation.listing.details.components.ListingDescription
import com.studhub.app.presentation.listing.details.components.ListingPrice
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel


@Composable
fun DetailedListingScreen(
    viewModel: DetailedListingViewModel = hiltViewModel(),
    navigateToConversation: (conversationId: String) -> Unit,
    navigateToRateUser: (userId: String) -> Unit,
    id: String?
) {
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
            Details(
                listing = listing,
                onFavoriteClicked = { viewModel.onFavoriteClicked() },
                isFavorite = isFavorite,
                onContactSellerClick = {
                    viewModel.contactSeller(listing.seller) { conv ->
                        navigateToConversation(conv.id)
                    }
                },
                onMeetingPointClick = {
                    val meetingPoint = listing.meetingPoint
                    if (meetingPoint != null) {
                        displayMeetingPoint(LatLng(meetingPoint.latitude, meetingPoint.longitude))
                    }
                },
                onRateUserClick = { navigateToRateUser(listing.seller.id) }
            )
        }
    }
}

@Composable
fun Details(
    onMeetingPointClick: () -> Unit,
    listing: Listing,
    onContactSellerClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit,
    onRateUserClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // "Contact seller" button
                BasicFilledButton(onClick = { onContactSellerClick() }, label = "Contact seller")
                // Rate user
                BasicFilledButton(onClick = { onRateUserClick() }, label = "Rate user")
                // "Favorite" button
                FavoriteButton(isFavorite = isFavorite, onFavoriteClicked = onFavoriteClicked)
            }

            Spacer("large")

            BigLabel(label = listing.name)

            Spacer("large")

            Carousel(modifier = Modifier.fillMaxWidth(0.8F), pictures = listing.pictures)

            Spacer("large")

            ListingDescription(description = listing.description)

            Spacer("large")

            ListingPrice(price = listing.price)

            Spacer(modifier = Modifier.height(80.dp))
            val meetingPoint = listing.meetingPoint
            if (meetingPoint != null) {
                Button(
                    onClick = onMeetingPointClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("View Meeting Point")
                }
            }
        }
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    val listing = Listing(
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
        onFavoriteClicked = { },
        isFavorite = true,
        onMeetingPointClick = {},
        onRateUserClick = {})
}
