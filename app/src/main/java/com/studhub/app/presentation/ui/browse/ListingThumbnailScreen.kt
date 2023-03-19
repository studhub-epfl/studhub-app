package com.studhub.app.presentation.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.ui.theme.StudHubTheme

@Composable
fun ListingThumbnailScreen(
    viewModel: ListingThumbnailViewModel,
    onClick: () -> Unit
) {
    StudHubTheme {
        ListingContent(listing = viewModel.listing, onClick = onClick)
    }
}

@Preview(showBackground = true)
@Composable
fun ListingThumbnailPreview() {
    val listing = Listing(
        name = "Scooter brand new from 2021",
        seller = User(firstName = "Jimmy", lastName = "Poppin"),
        categories = listOf(Category(name = "Mobility")),
        price = 1560.45F
    )

    val viewModel = ListingThumbnailViewModel(listing)
    ListingThumbnailScreen(viewModel = viewModel, onClick = {})
}
