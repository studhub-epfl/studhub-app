package com.studhub.app.presentation.listing.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User


@Composable
fun DetailedListingScreen(
    id: String,
    viewModel: DetailedListingViewModel,
    onContactSellerClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    navController: NavController
) {
    val listing = viewModel.detailedListingState.collectAsState().value

    if (listing != null) {
        ListingScreen(
            listing = listing,
            onContactSellerClick = onContactSellerClick,
            onFavouriteClick = onFavouriteClick
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchListing(id)
    }
}


@Preview(showBackground = true)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val listing = Listing(
        id = "1",
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
    val viewModel = DetailedListingViewModel(listingId = listing.id)
    DetailedListingScreen(
        id = listing.id,
        viewModel = viewModel,
        onContactSellerClick = { /*TODO*/ },
        onFavouriteClick = { /* TODO */ },
        navController = rememberNavController()
    )
}
