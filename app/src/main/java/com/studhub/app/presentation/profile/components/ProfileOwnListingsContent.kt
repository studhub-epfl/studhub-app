package com.studhub.app.presentation.profile.components

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
    navigateToListing: (id: String) -> Unit,
    isLoading: Boolean
) {
    Screen(
        title = stringResource(id = R.string.profile_own_listings_title),
        onGoBackClick = navigateToProfile,
        isLoading = isLoading
    ) {
        if (listings.isEmpty()) {
            Text(text = stringResource(id = R.string.profile_own_listings_no_listings))
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
}
