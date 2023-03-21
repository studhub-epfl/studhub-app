package com.studhub.app.presentation.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.studhub.app.presentation.home.HomeViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContentProviderCompat.requireContext
import com.studhub.app.R

@Composable
fun WelcomeText(viewModel: HomeViewModel) {
    val user = viewModel.currentUser.collectAsState()

    Text(
        text =
        if (user.value == null)
            stringResource(R.string.home_welcome_message)
        else
            String.format(
                stringResource(R.string.home_welcome_name_message),
                user.value!!.userName)
    )
}
