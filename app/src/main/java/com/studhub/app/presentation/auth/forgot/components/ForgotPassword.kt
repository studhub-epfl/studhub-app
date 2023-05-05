package com.studhub.app.presentation.auth.forgot.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.auth.AuthViewModel

@Composable
fun ForgotPassword(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showResetPasswordMessage: () -> Unit,
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when (val sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse) {
        is ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            val isPasswordResetEmailSent = sendPasswordResetEmailResponse.data
            LaunchedEffect(isPasswordResetEmailSent) {
                if (isPasswordResetEmailSent) {
                    navigateBack()
                    showResetPasswordMessage()
                }
            }
        }
        is ApiResponse.Failure -> {
            LaunchedEffect(sendPasswordResetEmailResponse) {
                showErrorMessage(sendPasswordResetEmailResponse.message)
            }
        }
    }
}
