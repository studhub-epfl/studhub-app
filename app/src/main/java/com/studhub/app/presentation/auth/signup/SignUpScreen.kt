package com.studhub.app.presentation.auth.signup

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.Utils.Companion.displayMessage
import com.studhub.app.presentation.auth.AuthViewModel
import com.studhub.app.presentation.auth.signup.components.SignUp
import com.studhub.app.presentation.auth.signup.components.SignUpContent
import com.studhub.app.presentation.auth.signup.components.SignUpTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalComposeUiApi
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SignUpTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            SignUpContent(
                padding = padding,
                signUp = { email, password ->
                    viewModel.signUpWithEmailAndPassword(email, password)
                },
                navigateBack = navigateBack
            )
        }
    )

    SignUp(
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        showVerifyEmailMessage = {
            displayMessage(context, "Verify email please")
        },
        showErrorMessage = { message ->
            displayMessage(context, message) }
    )
}
