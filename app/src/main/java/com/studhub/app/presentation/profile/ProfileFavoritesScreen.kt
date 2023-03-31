package com.studhub.app.presentation.profile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.studhub.app.R
import com.studhub.app.presentation.listing.browse.ListingThumbnailScreen
import com.studhub.app.presentation.listing.browse.ListingThumbnailViewModel
import com.studhub.app.presentation.ui.common.text.BigLabel


@Composable
fun ProfileFavoritesScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToListing: (id: String) -> Unit
) {
    val favorites = viewModel.userFavorites.collectAsState(initial = emptyList())
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BigLabel(label = stringResource(R.string.profile_favorites_title))

        if (favorites.value.isEmpty()) {
            BigLabel(label = stringResource(R.string.profile_favorites_no_favs))
        } else {
            LazyColumn {
                items(favorites.value) { listing ->
                    Spacer(modifier = Modifier.height(6.dp))
                    ListingThumbnailScreen(
                        viewModel = ListingThumbnailViewModel(listing = listing),
                        // to be removed
                        navController = rememberNavController(),
                        onClick = {
                            navigateToListing(listing.id)
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Divider()
                }
            }
        }
    }

}

