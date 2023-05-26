package com.studhub.app.presentation.auth.verify

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.Utils.Companion.displayMessage
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.auth.verify.components.ReloadUser
import com.studhub.app.presentation.auth.verify.components.VerifyEmailContent
import com.studhub.app.presentation.auth.verify.components.VerifyEmailTopBar

@Composable
fun VerifyEmailScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { VerifyEmailTopBar(onSignOut = { viewModel.signOut() }) },
        content = { padding ->
            VerifyEmailContent(
                padding = padding,
                reloadUser = {
                    viewModel.reloadUser()
                }
            )
        },
    )

    ReloadUser(
        navigateToProfileScreen = {
            if (viewModel.isEmailVerified) {
                navigateToProfileScreen()
            } else {
                displayMessage(
                    context,
                    context.getString(R.string.auth_verify_email_not_verified_error)
                )
            }
        }
    )
}
