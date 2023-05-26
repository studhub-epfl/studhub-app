package com.studhub.app.presentation.auth.verify.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.studhub.app.R
import com.studhub.app.presentation.ui.common.button.BasicFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailTopBar(onSignOut: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.auth_verify_title)
            )
        },
        actions = {
            BasicFilledButton(
                onClick = { onSignOut() },
                label = stringResource(R.string.auth_verify_btn_sign_out)
            )
        }
    )
}
