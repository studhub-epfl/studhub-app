package com.studhub.app.presentation.listing.browse

import BrowseContent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.ui.common.input.SearchBar
import com.studhub.app.presentation.ui.common.text.BigLabel
import kotlinx.coroutines.launch

@Composable
fun BrowseScreen(viewModel: BrowseViewModel = hiltViewModel(), navController: NavController) {
    BigLabel(label = stringResource(R.string.listings_browsing_title))
    val scope = rememberCoroutineScope()
    val search = remember {
        mutableStateOf("")
    }
    LaunchedEffect(viewModel) {

        /***
         *  If we want to use the fake listings,
         *  we can call viewModel.generateSampleListings()
         *  instead of viewModel.getAllListings()
         *
         */
        scope.launch {
            viewModel.getCurrentListings()
        }

    }
    val listings = viewModel.listingsState.collectAsState().value

    Column {
        SearchBar(search = search, onSearch = {
            viewModel.searchListings(search.value)
        })

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

}
