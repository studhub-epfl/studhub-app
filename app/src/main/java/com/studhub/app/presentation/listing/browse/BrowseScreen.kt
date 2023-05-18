package com.studhub.app.presentation.listing.browse

import BrowseContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.R
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.listing.browse.components.RangeBar
import com.studhub.app.presentation.listing.browse.components.SearchBar
import com.studhub.app.presentation.ui.common.text.BigLabel
import kotlinx.coroutines.launch

@Composable
fun BrowseScreen(viewModel: BrowseViewModel = hiltViewModel(), navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {

        /***
         *  If we want to use the fake listings,
         *  we can call viewModel.generateSampleListings()
         *  instead of viewModel.getAllListings()
         */
        scope.launch {
            viewModel.getCurrentListings()
        }
    }

    val listings = viewModel.listingsState.collectAsState().value
    BrowseScreenListings(viewModel, navController, listings)

}

/**
 * Main content of the screen where all the listings are displayed as well as the search fields
 *
 * @param viewModel takes a BrowseViewModel to be used to interact with the database
 * @param navController needs to be given to allow navigation when clicking a specific listing
 * @param listings the listings to be displayed
 */
@Composable
fun BrowseScreenListings(
    viewModel: BrowseViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    listings: List<Listing>
) {
    val search = remember {
        mutableStateOf("")
    }
    val rangeMin = remember {
        mutableStateOf("")
    }
    val rangeMax = remember {
        mutableStateOf("")
    }
    BigLabel(label = stringResource(R.string.listings_browsing_title))
    Column {
        SearchBar(search = search, onSearch = {
            viewModel.searchListings(search.value,
                                     rangeMin.value,
                                     rangeMax.value)
        })
        RangeBar("MIN....CHF", search = rangeMin, onSearch = {
            viewModel.searchListings(search.value,
                                     rangeMin.value,
                                     rangeMax.value)
        })
        RangeBar("MAX....CHF", search = rangeMax, onSearch = {
            viewModel.searchListings(search.value,
                                     rangeMin.value,
                                     rangeMax.value)
        })
        LoadListings(listings, navController)
    }
}

@Composable
fun LoadListings(listings: List<Listing>, navController: NavController) {
    if (listings.isNotEmpty()) {
        BrowseContent(listings, navController)
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
