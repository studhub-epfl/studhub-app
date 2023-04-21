package com.studhub.app.presentation.auth.forgot

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.Utils.Companion.displayMessage
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.auth.forgot.components.ForgotPassword
import com.studhub.app.presentation.auth.forgot.components.ForgotPasswordContent
import com.studhub.app.presentation.auth.forgot.components.ForgotPasswordTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email)
                }
            )
        }
    )

    ForgotPassword(
        navigateBack = navigateBack,
        showResetPasswordMessage = {
            displayMessage(context, context.getString(R.string.auth_forgot_reset_password_msg_sent))
        },
        showErrorMessage = { errorMessage ->
            displayMessage(context, errorMessage)
        }
    )
}
