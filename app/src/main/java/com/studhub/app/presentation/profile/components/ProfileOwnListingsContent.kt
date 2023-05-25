package com.studhub.app.presentation.profile.components

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.studhub.app.R
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.ui.common.container.Screen
import com.studhub.app.presentation.ui.common.misc.Spacer

@Composable
fun ProfileOwnListingsContent(
    listings: List<Listing>,
    drafts: List<Listing>,
    navigateToProfile: () -> Unit,
    navigateToListing: (id: String) -> Unit,
    navigateToDraft: (id: String) -> Unit,
    isLoading: Boolean
) {
    Screen(
        title = stringResource(R.string.profile_own_listings_title),
        onGoBackClick = navigateToProfile,
        isLoading = isLoading
    ) {
        ListingsList(
            title = stringResource(R.string.profile_own_listings_drafts_title),
            emptyText = stringResource(R.string.profile_own_listings_no_drafts),
            listings = drafts,
            navigateToListing = navigateToDraft
        )

        Spacer()
        Divider()
        Spacer()

        ListingsList(
            title = stringResource(R.string.profile_own_listings_published_title),
            emptyText = stringResource(R.string.profile_own_listings_no_listings),
            listings = listings,
            navigateToListing = navigateToListing
        )
    }
}
