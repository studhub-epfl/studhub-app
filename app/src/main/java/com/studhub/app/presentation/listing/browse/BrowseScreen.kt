package com.studhub.app.presentation.listing.browse

import BrowseContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.R
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.listing.add.components.CategorySheet
import com.studhub.app.presentation.listing.browse.components.RangeBar
import com.studhub.app.presentation.listing.browse.components.SearchBar
import com.studhub.app.presentation.listing.browse.components.SelectedCategories
import com.studhub.app.presentation.ui.common.button.PlusButton
import com.studhub.app.presentation.ui.common.text.BigLabel
import kotlinx.coroutines.launch

@Composable
fun BrowseScreen(viewModel: BrowseViewModel = hiltViewModel(), navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {

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
    val chosenCategories = remember { mutableStateListOf<Category>() }
    val isCategorySheetOpen = rememberSaveable { mutableStateOf(false) }
    val categories by viewModel.categories.collectAsState(emptyList())


    val rangeMinVal = rangeMin.value.takeIf { it.isNotEmpty() } ?: "0"
    val rangeMaxVal = rangeMax.value.takeIf { it.isNotEmpty() } ?: Float.MAX_VALUE.toString()

    fun onSearch() {
        viewModel.searchListings(
            search.value,
            rangeMinVal,
            rangeMaxVal,
            chosenCategories.toList()
        )
    }

    BigLabel(label = stringResource(R.string.listings_browsing_title))
    Column {
        SearchBar(search = search, onSearch = {onSearch()})
        Row (modifier = Modifier.fillMaxWidth()){
            RangeBar(stringResource(R.string.MIN____CHF), search = rangeMin, onSearch = {
                viewModel.searchListings(
                    search.value,
                    rangeMinVal,
                    rangeMaxVal,
                    chosenCategories.toList()
                )
            })
            RangeBar(stringResource(R.string.MAX____CHF), search = rangeMax, onSearch = {onSearch()})
        }
        Row {
            PlusButton(onClick = {isCategorySheetOpen.value = true})
            Text("Category filter:")
            SelectedCategories(categories = chosenCategories, onSelect={onSearch()})
        }
        CategorySheet(isOpen = isCategorySheetOpen, categories = categories, chosen = chosenCategories)
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
