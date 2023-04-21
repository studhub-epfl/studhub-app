package com.studhub.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.presentation.about.AboutScreen
import com.studhub.app.presentation.auth.AuthScreen
import com.studhub.app.presentation.auth.forgot.ForgotPasswordScreen
import com.studhub.app.presentation.auth.signup.SignUpScreen
import com.studhub.app.presentation.auth.verify.VerifyEmailScreen
import com.studhub.app.presentation.conversation.ConversationScreen
import com.studhub.app.presentation.home.HomeScreen
import com.studhub.app.presentation.listing.add.CreateListingScreen
import com.studhub.app.presentation.listing.browse.BrowseScreen
import com.studhub.app.presentation.listing.details.DetailedListingScreen
import com.studhub.app.presentation.profile.EditProfileScreen
import com.studhub.app.presentation.profile.ProfileFavoritesScreen
import com.studhub.app.presentation.profile.ProfileScreen

@OptIn(ExperimentalComposeUiApi::class)
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
                },
                navigateToForgotPasswordScreen = { navController.navigate("Auth/ForgotPassword") },
                navigateToSignUpScreen = { navController.navigate("Auth/SignUp") }
            )
        }

        composable(route = "Auth/SignUp") {
            SignUpScreen(navigateBack = { navController.navigate("Auth") })
        }

        composable(route = "Auth/ForgotPassword") {
            ForgotPasswordScreen(navigateBack = { navController.navigate("Auth") })
        }

        composable(route = "Auth/VerifyEmail") {
            VerifyEmailScreen(navigateToProfileScreen = { navController.navigate("EditProfile") })
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
                onConversationClick = { navController.navigate("Conversations") },
                onBrowseClick = { navController.navigate("Browse") },
                onAboutClick = { navController.navigate("About") },
                onCartClick = { navController.navigate("Cart") },
                onProfileClick = { navController.navigate("Profile") }
            )
        }
        composable("AddListing") {
            CreateListingScreen(navigateToListing = { id: String -> navController.navigate("DetailedListing/$id") })
        }
        composable("Browse") {

            BrowseScreen(navController = navController)
        }

        composable("About") {
            AboutScreen()
        }

        composable("DetailedListing/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            // TODO("Use conversation id to directly go to the discussion")
            DetailedListingScreen(id = id, navigateToConversation = { conversationId -> navController.navigate("Conversations") })
        }

        composable("Conversations") {
            ConversationScreen(navigateToDiscussion = { })
        }
    }
}
