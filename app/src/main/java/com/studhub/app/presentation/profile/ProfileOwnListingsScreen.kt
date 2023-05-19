package com.studhub.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.profile.components.ProfileOwnListingsContent
import com.studhub.app.presentation.ui.common.misc.LoadingCircle

@Composable
fun ProfileOwnListingsScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfile: () -> Unit,
    navigateToListing: (id: String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getOwnListings()
    }

    when (val ownListings = viewModel.ownListings) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success ->
            ProfileOwnListingsContent(
                listings = ownListings.data,
                navigateToProfile = navigateToProfile,
                navigateToListing = navigateToListing
            )
    }
}
