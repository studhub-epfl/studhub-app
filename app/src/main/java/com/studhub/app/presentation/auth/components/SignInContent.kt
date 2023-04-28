package com.studhub.app.presentation.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.input.EmailTextField
import com.studhub.app.presentation.ui.common.input.PasswordTextField
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
@ExperimentalComposeUiApi
fun SignInContent(
    padding: PaddingValues,
    signIn: (email: String, password: String) -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    val keyboard = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BigLabel(label = stringResource(id = R.string.auth_signin_welcome_message))

            Spacer("large")

            EmailTextField(
                email = email,
                onEmailValueChange = { newValue ->
                    email = newValue
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                password = password,
                onPasswordValueChange = { newValue ->
                    password = newValue
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    keyboard?.hide()
                    signIn(email.text, password.text)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.auth_signin_btn_submit),
                    fontSize = 15.sp
                )
            }
            Row {
                Text(
                    modifier = Modifier.clickable {
                        navigateToForgotPasswordScreen()
                    },
                    text = stringResource(id = R.string.auth_signin_btn_forgot_password),
                    fontSize = 15.sp
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    text = " | ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.clickable {
                        navigateToSignUpScreen()
                    },
                    text = stringResource(id = R.string.auth_signin_btn_no_account),
                    fontSize = 15.sp
                )
            }
        }
    }
}
