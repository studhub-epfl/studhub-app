package com.studhub.app.presentation.auth.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun AuthTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Authentication"
            )
        }
    )
}
