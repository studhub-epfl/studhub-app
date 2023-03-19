package com.studhub.app.presentation.ui.browse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun BrowseScreen(viewModel: BrowseViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        /***
         *  If we want to use the fake listings,
         *  we can call viewModel.generateSampleListings()
         *  instead of viewModel.getAllListings()
         *
         */
        scope.launch {
            viewModel.generateSampleListings()
        }

    }
    val listings = viewModel.listingsState.collectAsState().value
    if (listings.isNotEmpty()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                items(listings) { listing ->
                    Spacer(modifier = Modifier.height(6.dp))
                    ListingThumbnailScreen(
                        viewModel = ListingThumbnailViewModel(listing = listing),
                        onClick = {
                            viewModel.onListingClick(context, listing)
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Divider()
                }
            }
        }
    } else {
        // Show a loading indicator while the listings are being fetched.
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}


@Preview(showBackground = true)
@Composable
fun BrowseActivityPreview() {
    val viewModel = BrowseViewModel()
    BrowseScreen(viewModel = viewModel)
}
