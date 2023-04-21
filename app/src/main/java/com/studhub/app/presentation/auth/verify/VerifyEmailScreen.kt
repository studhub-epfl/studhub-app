package com.studhub.app.presentation.auth.verify

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.Utils.Companion.displayMessage
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.auth.verify.components.ReloadUser
import com.studhub.app.presentation.auth.verify.components.VerifyEmailContent
import com.studhub.app.presentation.ui.common.button.BasicFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.auth_verify_title)
                    )
                },
                actions = {
                    BasicFilledButton(
                        onClick = { viewModel.signOut() },
                        label = stringResource(R.string.auth_verify_btn_sign_out)
                    )
                }
            )
        },
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
