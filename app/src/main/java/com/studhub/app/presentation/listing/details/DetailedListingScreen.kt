package com.studhub.app.presentation.listing.details

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.MeetingPointPickerActivity
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.details.components.DetailsButtons
import com.studhub.app.presentation.listing.details.components.ListingDescription
import com.studhub.app.presentation.listing.details.components.ListingImage
import com.studhub.app.presentation.listing.details.components.ListingPrice
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel



@Composable
fun DetailedListingScreen(
    viewModel: DetailedListingViewModel = hiltViewModel(),
    navigateToConversation: (conversationId: String) -> Unit,
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
            Details(
                listing = listing,
                onContactSellerClick = {
                    viewModel.contactSeller(listing.seller) { conv ->
                        navigateToConversation(conv.id)
                    }
                },
                onFavouriteClick = { /* TODO */ },
                onMeetingPointClick = {
                    val meetingPoint = listing.meetingPoint
                    if (meetingPoint != null) {
                        displayMeetingPoint(LatLng(meetingPoint.latitude, meetingPoint.longitude))
                    }
                })
        }
    }
}

@Composable
fun Details(
    listing: Listing, onContactSellerClick: () -> Unit, onFavouriteClick: () -> Unit,
    onMeetingPointClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DetailsButtons(onContactSellerClick, onFavouriteClick)

            Spacer("large")

            BigLabel(label = listing.name)

            // Add the placeholder image here
            ListingImage(contentDescription = "Item picture")

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
        onFavouriteClick = { },
        onMeetingPointClick = {})
}
