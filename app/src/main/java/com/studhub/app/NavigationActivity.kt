package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.ui.AddListingScreen
import com.studhub.app.ui.BrowseScreen
import com.studhub.app.ui.HomeScreen
import com.studhub.app.ui.theme.StudHubTheme
import com.studhub.app.ui.ListingScreen

// we don't have listings yet so this is mandatory to test, will remove later.
val listing = Listing(
    id = 1,
    name = "iPhone 13",
    description = "The latest iPhone with a 6.1-inch Super Retina XDR display.",
    seller = User(),
    price = 999.99F,
    categories = listOf(
        Category(name = "Electronics"),
        Category(name = "Smartphones")
    )
)
@Preview
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Listing") {
        composable("Home") {
            HomeScreen(
                onAddListingClick = { navController.navigate("AddListing") },
                onBrowseClick = { navController.navigate("Browse") }
            )
        }
        composable("AddListing") {
            AddListingScreen()
        }
        composable("Browse") {
            BrowseScreen()
        }
        composable("Listing"){
            ListingScreen(listing)
        }
    }
}



