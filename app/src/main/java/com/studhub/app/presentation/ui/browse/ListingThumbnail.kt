package com.studhub.app.presentation.ui.browse

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User

@Composable
fun ListingThumbnail(listing: Listing) {
    Row {

    }
}

@Preview(showBackground = true)
@Composable
fun ListingThumbnailPreview() {
    val listing = Listing(
        name = "Scooter",
        seller = User(firstName = "Jimmy", lastName = "Poppin"),
        categories = listOf(Category(name = "Mobility")),
        price = 1560.45F
    )
    ListingThumbnail(listing = listing)
}
