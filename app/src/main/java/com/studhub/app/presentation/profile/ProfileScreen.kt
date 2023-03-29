package com.studhub.app.presentation.profile

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.profile.components.ProfileContent
import com.studhub.app.presentation.profile.components.ProfileTopBar
import com.studhub.app.presentation.profile.components.SignOut
import com.studhub.app.presentation.ui.common.misc.LoadingCircle

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToAuthScreen: () -> Unit,
    navigateToEditProfileScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    when (val currentUser = viewModel.currentUser) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            Scaffold(
                topBar = {
                    ProfileTopBar(
                        editProfile = navigateToEditProfileScreen,
                        signOut = {
                            viewModel.signOut()
                        }
                    )
                },
                content = { padding ->
                    ProfileContent(
                        padding = padding,
                        profile = currentUser.data
                    )
                },
                scaffoldState = scaffoldState
            )

            SignOut(
                navigateToAuthScreen = { signedOut ->
                    if (signedOut) {
                        navigateToAuthScreen()
                    }
                }
            )
        }
    }
}
