package com.studhub.app.presentation.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.studhub.app.presentation.home.HomeViewModel
import androidx.compose.material3.Text

@Composable
fun WelcomeText(viewModel: HomeViewModel) {
    val user = viewModel.currentUser.collectAsState()

    Text(
        text =
        if (user.value == null)
            "Welcome to our app!"
        else
            "Welcome to our app ${user.value!!.userName}!"
    )
}
