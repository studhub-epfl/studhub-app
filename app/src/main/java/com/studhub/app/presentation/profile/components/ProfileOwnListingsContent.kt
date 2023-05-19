package com.studhub.app.presentation.profile.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.studhub.app.R
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.ui.browse.ListingCard
import com.studhub.app.presentation.ui.common.container.Screen
import com.studhub.app.presentation.ui.common.misc.Spacer

@Composable
fun ProfileOwnListingsContent(
    listings: List<Listing>,
    navigateToProfile: () -> Unit,
    navigateToListing: (id: String) -> Unit
) {
    Screen(
        title = stringResource(id = R.string.profile_own_listings_title),
        onGoBackClick = navigateToProfile
    ) {
        if (listings.isEmpty()) {
            Text(text = stringResource(id = R.string.profile_own_listings_no_listings))
        } else {
            LazyColumn {
                items(listings) { listing ->
                    Spacer("small")

                    ListingCard(
                        listing,
                        onClick = {
                            navigateToListing(listing.id)
                        }
                    )

                    Spacer("small")
                    Divider()
                }
            }
        }
    }

}
