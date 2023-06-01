package com.studhub.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.profile.components.ListingsList
import com.studhub.app.presentation.ui.common.container.Screen


@Composable
fun ProfileFavoritesScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToListing: (id: String) -> Unit,
    navigateToProfile: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    val isLoading = viewModel.favoriteListings !is ApiResponse.Success

    ProfileFavoritesContent(
        favorites = if (isLoading) emptyList() else (viewModel.favoriteListings as ApiResponse.Success).data,
        navigateToListing = navigateToListing,
        isLoading = isLoading,
        navigateToProfile = navigateToProfile,
    )
}

@Composable
fun ProfileFavoritesContent(
    favorites: List<Listing>,
    navigateToListing: (id: String) -> Unit,
    isLoading: Boolean,
    navigateToProfile: () -> Unit
) {
    Screen(
        title = stringResource(R.string.profile_favorites_title),
        onGoBackClick = navigateToProfile,
        isLoading = isLoading
    ) {
        ListingsList(
            title = stringResource(R.string.profile_favorites_title),
            emptyText = stringResource(R.string.profile_favorites_no_favs),
            listings = favorites,
            navigateToListing = navigateToListing
        )
    }
}
