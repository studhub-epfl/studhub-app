package com.studhub.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studhub.app.core.Globals
import com.studhub.app.presentation.about.AboutScreen
import com.studhub.app.presentation.auth.AuthScreen
import com.studhub.app.presentation.auth.forgot.ForgotPasswordScreen
import com.studhub.app.presentation.auth.signup.SignUpScreen
import com.studhub.app.presentation.auth.verify.VerifyEmailScreen
import com.studhub.app.presentation.conversation.ChatScreen
import com.studhub.app.presentation.conversation.ConversationScreen
import com.studhub.app.presentation.home.HomeScreen
import com.studhub.app.presentation.listing.add.CreateListingScreen
import com.studhub.app.presentation.listing.browse.BrowseScreen
import com.studhub.app.presentation.listing.details.DetailedListingScreen
import com.studhub.app.presentation.profile.EditProfileScreen
import com.studhub.app.presentation.profile.ProfileFavoritesScreen
import com.studhub.app.presentation.profile.ProfileScreen
import com.studhub.app.presentation.ratings.UserRatingScreen
import com.studhub.app.presentation.ratings.UserRatingViewModel

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
            Globals.showBottomBar = false
            SignUpScreen(navigateBack = { navController.navigate("Auth") })
        }

        composable(route = "Auth/ForgotPassword") {
            Globals.showBottomBar = false
            ForgotPasswordScreen(navigateBack = { navController.navigate("Auth") })
        }

        composable(route = "Auth/VerifyEmail") {
            Globals.showBottomBar = false
            VerifyEmailScreen(navigateToProfileScreen = { navController.navigate("EditProfile") })
        }

        composable(route = "Profile") {
            Globals.showBottomBar = true
            ProfileScreen(
                navigateToAuthScreen = { navController.navigate("Auth") },
                navigateToEditProfileScreen = { navController.navigate("EditProfile") },
                navigateToProfileFavorites = { navController.navigate("Profile/Favorite-Listing") }
            )
        }

        composable(route = "Profile/Favorite-Listing") {
            Globals.showBottomBar = true
            ProfileFavoritesScreen(navigateToListing = { id: String -> navController.navigate("Listing/$id") })
        }

        composable(route = "EditProfile") {
            Globals.showBottomBar = false
            EditProfileScreen(navigateToProfile = { navController.navigate("Profile") })
        }

        composable("Home") {
            Globals.showBottomBar = true
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
            Globals.showBottomBar = false
            CreateListingScreen(
                navigateToListing = { id: String -> navController.navigate("DetailedListing/$id") },
                navigateBack = { navController.popBackStack() })
        }
        composable("Browse") {
            Globals.showBottomBar = true
            BrowseScreen(navController = navController)
        }

        composable("About") {
            AboutScreen()
        }

        composable("DetailedListing/{id}") { backStackEntry ->
            Globals.showBottomBar = false
            val id = backStackEntry.arguments?.getString("id")

        }
        composable("Conversations") {
            Globals.showBottomBar = true
            ConversationScreen(navigateToDiscussion = { conversationId -> navController.navigate("Conversations/$conversationId") })
        }


//        composable("RatingScreen") {
//            UserRatingScreen(targetUserId = "-NRpD74U8sedQAH5_hdn")
//        }

        composable("RatingScreen/{targetUserId}") { backStackEntry ->
            val targetUserId = backStackEntry.arguments?.getString("targetUserId")
            if (targetUserId != null) {
                UserRatingScreen(targetUserId = targetUserId)
            }
        }
    }
}
