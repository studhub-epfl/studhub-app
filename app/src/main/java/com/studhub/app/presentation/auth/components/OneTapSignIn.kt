package com.studhub.app.presentation.auth.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.ui.common.ProgressBar

@Composable
fun OneTapSignIn(
    viewModel: AuthViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit
) {
    when (val oneTapSignInResponse = viewModel.oneTapSignInResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> oneTapSignInResponse.data.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is ApiResponse.Failure -> LaunchedEffect(Unit) {
            print(oneTapSignInResponse.message)
        }
    }
}
