package com.studhub.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.profile.components.ProfileOwnListingsContent

@Composable
fun ProfileOwnListingsScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfile: () -> Unit,
    navigateToListing: (id: String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getOwnListings()
        viewModel.getOwnDraftListings()
    }

    val isLoading =
        viewModel.ownListings !is ApiResponse.Success || viewModel.ownDraftListings !is ApiResponse.Success

    ProfileOwnListingsContent(
        listings = if (isLoading) emptyList() else (viewModel.ownListings as ApiResponse.Success).data,
        drafts = if (isLoading) emptyList() else (viewModel.ownDraftListings as ApiResponse.Success).data,
        navigateToProfile = navigateToProfile,
        navigateToListing = navigateToListing,
        isLoading = isLoading
    )
}
