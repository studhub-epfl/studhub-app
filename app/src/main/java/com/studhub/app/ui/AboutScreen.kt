package com.studhub.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.ui.theme.StudHubTheme

@Composable
fun AboutScreen() {
    StudHubTheme {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "About Us:")
            Spacer(Modifier.height(16.dp))
            Text(text = "We are a MarketPlace company working for EPFL campus.")
            Spacer(Modifier.height(16.dp))
            Text(text = "Contact us at ...")
        }
    }
}
