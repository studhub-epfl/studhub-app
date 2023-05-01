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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.studhub.app.MeetingPointPickerActivity
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.details.components.DetailsButtons
import com.studhub.app.presentation.listing.details.components.ListingDescription
import com.studhub.app.presentation.listing.details.components.ListingImage
import com.studhub.app.presentation.listing.details.components.ListingPrice
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel


@Composable
fun DetailedListingScreen(
    viewModel: DetailedListingViewModel = hiltViewModel(),
    id: String
) {
  val currentListing = viewModel.currentListing

    LaunchedEffect(id) {
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

    if (currentListing is ApiResponse.Success) {
        val listing = (currentListing as ApiResponse.Success<Listing>).data
        Details(
            listing = listing,
            onContactSellerClick = { /*TODO*/ },
            onFavouriteClick = { /* TODO */ })

        val meetingPoint = listing.meetingPoint
        if (meetingPoint != null) {
            Button(
                onClick = {
                    displayMeetingPoint(LatLng(meetingPoint.latitude, meetingPoint.longitude))
                }
            ) {
                Text("View Meeting Point")
            }
        }
    } else {
        LoadingCircle()
    }
}



@Composable
fun Details(
    listing: Listing, onContactSellerClick: () -> Unit, onFavouriteClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DetailsButtons(onContactSellerClick, onFavouriteClick)
            Spacer(modifier = Modifier.height(24.dp))
            BigLabel(label = listing.name)
            // Add the placeholder image here
            ListingImage(contentDescription = "Item picture")
            Spacer(modifier = Modifier.height(30.dp))
            ListingDescription(description = listing.description)
            Spacer(modifier = Modifier.height(35.dp))
            ListingPrice(price = listing.price)
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
        price = 545.45F,
        meetingPoint = MeetingPoint((1.0), 1.0)
    )
    Details(
        listing = listing,
        onContactSellerClick = { },
        onFavouriteClick = { })


}
