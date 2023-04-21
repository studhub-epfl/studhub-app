package com.studhub.app.presentation.auth.forgot.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.input.EmailTextField
import com.studhub.app.presentation.ui.common.misc.Spacer

@Composable
fun ForgotPasswordContent(
    padding: PaddingValues,
    sendPasswordResetEmail: (email: String) -> Unit,
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailTextField(
            email = email,
            onEmailValueChange = { newValue ->
                email = newValue
            }
        )

        Spacer("small")

        Button(
            onClick = {
                sendPasswordResetEmail(email.text)
            }
        ) {
            Text(
                text = stringResource(id = R.string.auth_forgot_btn_submit),
                fontSize = 15.sp
            )
        }
    }
}
