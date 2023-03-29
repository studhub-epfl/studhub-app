package com.studhub.app.presentation.auth.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.ui.common.misc.LoadingCircle

@Composable
fun SignInWithGoogle(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignIn: (isNewUser: Boolean) -> Unit,
) {
    when (val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Success -> signInWithGoogleResponse.data.let { isNewUser ->
            LaunchedEffect(isNewUser) {
                onSignIn(isNewUser)
            }
        }
        is ApiResponse.Failure -> LaunchedEffect(Unit) {
            print(signInWithGoogleResponse.message)
        }
    }
}
