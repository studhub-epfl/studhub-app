package com.studhub.app

import DetailedListingViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.auth.AuthScreen
import com.studhub.app.presentation.home.HomeScreen
import com.studhub.app.presentation.ui.browse.BrowseScreen
import com.studhub.app.presentation.ui.browse.BrowseViewModel
import com.studhub.app.presentation.ui.detailedlisting.DetailedListingScreen
import com.studhub.app.presentation.ui.listing.CreateListingScreen
import com.studhub.app.presentation.ui.listing.CreateListingViewModel
import com.studhub.app.ui.AboutScreen
import com.studhub.app.ui.CartScreen

// we don't have listings yet so this is mandatory to test, will remove later.
val listing = Listing(
    id = "1",
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
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Auth"
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(
            route = "Auth"
        ) {
            AuthScreen(
                onLoginComplete = {
                    navController.navigate("Home")
                }
            )
        }

        composable("Home") {
            HomeScreen(
                onAddListingClick = { navController.navigate("AddListing") },
                onBrowseClick = { navController.navigate("Browse") },
                onAboutClick = { navController.navigate("About") },
                onCartClick = { navController.navigate("Cart") }
            )
        }
        composable("AddListing") {
            val createListingViewModel = CreateListingViewModel()
            CreateListingScreen(viewModel = createListingViewModel)
        }
        composable("Browse") {
            val browseViewModel = BrowseViewModel()
            BrowseScreen(viewModel = browseViewModel, navController = navController)
        }
        composable("Cart") {
            CartScreen()
        }
        composable("About") {
            AboutScreen()
        }

        composable("Listing/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val detailedListingViewModel = remember { DetailedListingViewModel(listingId = id) }
            DetailedListingScreen(
                id = "id",
                viewModel = detailedListingViewModel,
                onContactSellerClick = {},
                onFavouriteClick = {},
                navController = rememberNavController()
            )
        }


    }
}



