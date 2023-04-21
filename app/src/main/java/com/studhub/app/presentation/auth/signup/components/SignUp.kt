package com.studhub.app.presentation.auth.signup.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.auth.AuthViewModel

@Composable
fun SignUp(
    viewModel: AuthViewModel = hiltViewModel(),
    sendEmailVerification: () -> Unit,
    showVerifyEmailMessage: () -> Unit,
    showErrorMessage: (message: String?) -> Unit,
) {
    when (val signUpResponse = viewModel.signUpResponse) {
        is ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            val isUserSignedUp = signUpResponse.data
            LaunchedEffect(isUserSignedUp) {
                if (isUserSignedUp) {
                    sendEmailVerification()
                    showVerifyEmailMessage()
                }
            }
        }
        is ApiResponse.Failure -> {
            showErrorMessage(signUpResponse.message)
        }
    }
}
