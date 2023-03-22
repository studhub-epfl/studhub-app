package com.studhub.app.presentation.home.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.studhub.app.R
import com.studhub.app.domain.model.User

@Composable
fun WelcomeText(user: User?) {
    Text(
        text =
        if (user == null)
            stringResource(R.string.home_welcome_message)
        else
            String.format(
                stringResource(R.string.home_welcome_name_message),
                user.userName
            )
    )
}
