package com.studhub.app.presentation.auth.signup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.input.EmailTextField
import com.studhub.app.presentation.ui.common.input.PasswordTextField
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
@ExperimentalComposeUiApi
fun SignUpContent(
    padding: PaddingValues,
    signUp: (email: String, password: String) -> Unit,
    navigateBack: () -> Unit
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BigLabel(label = stringResource(id = R.string.auth_signup_info_title))

        Spacer("large")

        Text(
            text = stringResource(id = R.string.auth_signup_info_description)
        )

        Spacer("small")

        EmailTextField(
            email = email,
            onEmailValueChange = { newValue ->
                email = newValue
            }
        )

        Spacer("small")

        PasswordTextField(
            password = password,
            onPasswordValueChange = { newValue ->
                password = newValue
            }
        )

        Spacer("small")

        Button(
            onClick = {
                keyboard?.hide()
                signUp(email.text, password.text)
            }
        ) {
            Text(
                text = stringResource(id = R.string.auth_signup_btn_submit),
                fontSize = 15.sp
            )
        }

        Text(
            modifier = Modifier.clickable {
                navigateBack()
            },
            text = stringResource(id = R.string.auth_signup_already_have_account),
            fontSize = 15.sp
        )
    }
}
