package com.studhub.app.presentation.listing.browse

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.ListingType
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.listing.browse.components.ListingCard
import com.studhub.app.presentation.ui.theme.StudHubTheme

@Composable
fun ListingThumbnailScreen(
    viewModel: ListingThumbnailViewModel,
    onClick: () -> Unit,
) {
    ListingCard(listing = viewModel.listing, onClick = onClick)
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun ListingThumbnailPreview() {
    val listing = Listing(
        name = "Scooter brand new from 2021",
        seller = User(firstName = "Jimmy", lastName = "Poppin"),
        categories = listOf(Category(name = "Mobility")),
        price = 1560.45F
    )
    val listing2 = Listing(
        name = "Limited edition manga",
        seller = User(firstName = "Jimmy", lastName = "Poppin"),
        categories = listOf(Category(name = "Books")),
        type = ListingType.BIDDING,
        price = 80F
    )

    StudHubTheme {
        Column {
            ListingCard(listing = listing, onClick = {})
            ListingCard(listing = listing2, onClick = {})
        }
    }
}
