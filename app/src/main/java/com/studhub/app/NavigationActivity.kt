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
import com.studhub.app.ui.*
import com.studhub.app.ui.theme.StudHubTheme

// we don't have listings yet so this is mandatory to test, will remove later.
val listing = Listing(
    id = 1L,
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

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(
                onAddListingClick = { navController.navigate("AddListing") },
                onBrowseClick = { navController.navigate("Browse") },
                onAboutClick ={navController.navigate("About")},
                onCartClick ={navController.navigate("Cart")}
            )
        }
        composable("AddListing") {
            AddListingScreen()
        }
        composable("Browse") {
            BrowseScreen()
        }
        composable("Cart"){
            CartScreen()
        }
        composable("About"){
            AboutScreen()
        }

        composable("Listing") {
            ListingScreen(
                listing = listing,
                onContactSellerClick = {
                    // Implement the action to contact the seller
                },
                onFavouriteClick = {
                    // Implement the action to favourite the listing
                }
            )
        }
    }
}



