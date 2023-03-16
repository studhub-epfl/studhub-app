package com.studhub.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.ui.theme.StudHubTheme

@Composable
fun BrowseScreen() {
    StudHubTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Browse our selection:")
            Spacer(Modifier.height(16.dp))
            // TODO: Add category list
        }
    }
}
