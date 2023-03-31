package com.studhub.app

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
import com.studhub.app.presentation.about.AboutScreen
import com.studhub.app.presentation.auth.AuthScreen
import com.studhub.app.presentation.cart.CartScreen
import com.studhub.app.presentation.home.HomeScreen
import com.studhub.app.presentation.listing.add.CreateListingScreen
import com.studhub.app.presentation.listing.add.CreateListingViewModel
import com.studhub.app.presentation.listing.browse.BrowseScreen
import com.studhub.app.presentation.listing.browse.DetailedListingScreen
import com.studhub.app.presentation.listing.browse.DetailedListingViewModel
import com.studhub.app.presentation.profile.EditProfileScreen
import com.studhub.app.presentation.profile.ProfileScreen
import com.studhub.app.presentation.profile.ProfileFavoritesScreen

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
                onLoginComplete = { isNewUser ->
                    if (isNewUser) {
                        navController.navigate("EditProfile")
                    } else {
                        navController.navigate("Home")
                    }
                }
            )
        }

        composable(route = "Profile") {
            ProfileScreen(
                navigateToAuthScreen = { navController.navigate("Auth") },
                navigateToEditProfileScreen = { navController.navigate("EditProfile") },
                navigateToProfileFavorites = { navController.navigate("Profile/Favorite-Listing") }
            )
        }

        composable(route = "Profile/Favorite-Listing") {
            ProfileFavoritesScreen(navigateToListing = { id: String -> navController.navigate("Listing/$id") })
        }

        composable(route = "EditProfile") {
            EditProfileScreen(navigateToProfile = { navController.navigate("Profile") })
        }

        composable("Home") {
            HomeScreen(
                onAddListingClick = { navController.navigate("AddListing") },
                onBrowseClick = { navController.navigate("Browse") },
                onAboutClick = { navController.navigate("About") },
                onCartClick = { navController.navigate("Cart") },
                onProfileClick = { navController.navigate("Profile") }
            )
        }
        composable("AddListing") {
            val createListingViewModel = CreateListingViewModel()
            CreateListingScreen(viewModel = createListingViewModel)
        }
        composable("Browse") {

            BrowseScreen(navController = navController)
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
