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

    val isLoading = viewModel.ownListings !is ApiResponse.Success

    ProfileOwnListingsContent(
        listings = if (isLoading) emptyList() else (viewModel.ownListings as ApiResponse.Success).data,
        navigateToProfile = navigateToProfile,
        navigateToListing = navigateToListing,
        isLoading = isLoading
    )
}
