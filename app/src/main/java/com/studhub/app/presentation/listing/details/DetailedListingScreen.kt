package com.studhub.app.presentation.listing.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.details.components.*
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel


@Composable
fun DetailedListingScreen(
    viewModel: DetailedListingViewModel = hiltViewModel(),
    id: String
) {
    LaunchedEffect(id) {
        viewModel.fetchListing(id)
    }

    when (val currentListing = viewModel.currentListing) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            val listing = currentListing.data
            val isFavorite = viewModel.isFavorite.value
            Details(
                listing = listing,
                onContactSellerClick = { /*TODO*/ },
                onFavoriteClicked = {viewModel.onFavoriteClicked()},
                isFavorite = isFavorite
            )
        }
    }
}

@Composable
fun Details(
    listing: Listing,
    onContactSellerClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // "Contact seller" button
                BasicFilledButton(onClick = { onContactSellerClick() }, label = "Contact seller")
                // "Favorite" button
                FavoriteButton(isFavorite = isFavorite, onFavoriteClicked = onFavoriteClicked)
            }
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
        price = 545.45F
    )
    Details(
        listing = listing,
        onContactSellerClick = { },
        onFavoriteClicked = { },
        isFavorite = true,
        )
}
