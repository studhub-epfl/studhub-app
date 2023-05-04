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
import com.studhub.app.presentation.listing.browse.components.RangeBar
import com.studhub.app.presentation.listing.browse.components.SearchBar
import com.studhub.app.presentation.ui.common.text.BigLabel
import kotlinx.coroutines.launch

@Composable
fun BrowseScreen(viewModel: BrowseViewModel = hiltViewModel(), navController: NavController) {
    BigLabel(label = stringResource(R.string.listings_browsing_title))
    val scope = rememberCoroutineScope()
    val search1 = remember {
        mutableStateOf("")
    }
    val search2 = remember {
        mutableStateOf("")
    }
    val search3 = remember {
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
        SearchBar(search = search1, onSearch = {
            viewModel.searchListings(search1.value)
        })
        RangeBar("MIN....CHF", search = search2, onSearch = {
            viewModel.rangeListings(search2.value, search3.value)
        })
        RangeBar("MAX....CHF",search = search3, onSearch = {
            viewModel.rangeListings(search2.value, search3.value)
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
