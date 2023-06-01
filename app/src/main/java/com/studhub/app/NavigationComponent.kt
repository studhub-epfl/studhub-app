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
import com.studhub.app.presentation.profile.*
import com.studhub.app.presentation.ratings.UserRatingScreen

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
            VerifyEmailScreen(navigateToProfileScreen = { navController.navigate("Profile/Edit") })
        }

        composable(route = "Profile") {
            Globals.showBottomBar = true
            ProfileScreen(
                navigateToAuthScreen = { navController.navigate("Auth") },
                navigateToEditProfileScreen = { navController.navigate("Profile/Edit") },
                navigateToProfileFavorites = { navController.navigate("Profile/Favorite-Listing") },
                navigateToOwnListings = { navController.navigate("Profile/Own-Listings") },
                navigateToBlockedUsers = { navController.navigate("Profile/Blocked-Users") }
            )
        }

        composable(route = "Profile/Favorite-Listing") {
            Globals.showBottomBar = true
            ProfileFavoritesScreen(navigateToListing = { id: String ->
                navController.navigate("Listing/$id")
            })
        }

        composable(route = "Profile/Own-Listings") {
            Globals.showBottomBar = false
            ProfileOwnListingsScreen(
                navigateToProfile = { navController.navigate("Profile") },
                navigateToListing = { id: String -> navController.navigate("Listing/$id") },
                navigateToDraft = { id: String -> navController.navigate("Listing/Add/$id")}
            )
        }

        composable(route = "Profile/Blocked-Users") {
            Globals.showBottomBar = true
            ProfileBlockedScreen()
        }

        composable(route = "Profile/Edit") {
            Globals.showBottomBar = false
            EditProfileScreen(navigateToProfile = { navController.navigate("Profile") })
        }

        composable("Home") {
            Globals.showBottomBar = true
            HomeScreen(
                onAddListingClick = { navController.navigate("Listing/Add") },
                onConversationClick = { navController.navigate("Conversations") },
                onBrowseClick = { navController.navigate("Listing") },
                onAboutClick = { navController.navigate("About") },
                onProfileClick = {
                    navController.navigate("Profile")
                }
            )
        }

        composable("Listing") {
            Globals.showBottomBar = true
            BrowseScreen(navController = navController)
        }

        composable("Listing/{id}") { backStackEntry ->
            Globals.showBottomBar = true
            val id = backStackEntry.arguments?.getString("id")
            if (id == null) {
                navController.navigate("Listing")
                return@composable
            }

            DetailedListingScreen(
                id = id,
                navigateToConversation = { conversationId -> navController.navigate("Conversations/$conversationId") },
                navigateToRateUser = { userId -> navController.navigate("RatingScreen/$userId") }
            )
        }

        composable("Listing/Add") {
            Globals.showBottomBar = false
            CreateListingScreen(
                navigateToListing = { id: String -> navController.navigate("Listing/$id") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable("Listing/Add/{draftId}") {
            Globals.showBottomBar = false
            val draftId = it.arguments?.getString("draftId")
            CreateListingScreen(
                navigateToListing = { id: String -> navController.navigate("Listing/$id") },
                navigateBack = { navController.popBackStack() },
                draftId = draftId
            )
        }

        composable("About") {
            AboutScreen()
        }

        composable("Conversations") {
            Globals.showBottomBar = true
            ConversationScreen(navigateToDiscussion = { conversationId -> navController.navigate("Conversations/$conversationId") })
        }

        composable("Conversations/{id}") {
            Globals.showBottomBar = false
            val id = it.arguments?.getString("id")
            if (id == null) {
                navController.navigate("Conversations")
                return@composable
            }

            ChatScreen(conversationId = id, navigateBack = { navController.popBackStack() })
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
