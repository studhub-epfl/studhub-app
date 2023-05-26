package com.studhub.app.presentation.profile.components

import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.listing.browse.components.ListingCard
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
fun ListingsList(
    title: String,
    emptyText: String,
    listings: List<Listing>,
    navigateToListing: (id: String) -> Unit
) {
    BigLabel(label = title)

    if (listings.isEmpty()) {
        Text(text = emptyText)
    } else {
        listings.map {
            Spacer("small")

            ListingCard(
                listing = it,
                onClick = {
                    navigateToListing(it.id)
                }
            )

            Spacer("small")
            Divider()
        }
    }

}
