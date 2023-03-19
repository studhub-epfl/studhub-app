package com.studhub.app.presentation.ui.browse

import BrowseContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun BrowseScreen(viewModel: BrowseViewModel, navController: NavController) {
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



@Preview(showBackground = true)
@Composable
fun BrowseActivityPreview() {
   val viewModel = BrowseViewModel()
    lateinit var navController: NavHostController
    navController = rememberNavController()
    BrowseScreen(viewModel = viewModel, navController = navController)
}
