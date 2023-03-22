package com.studhub.app.presentation.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.presentation.ui.theme.StudHubTheme

@Composable
fun AboutScreen() {
    StudHubTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Text(text = stringResource(R.string.about_title))
            Spacer(Modifier.height(16.dp))
            Text(text = stringResource(R.string.about_content))
            Spacer(Modifier.height(16.dp))
            Text(text = stringResource(R.string.about_contact_title))
            Spacer(Modifier.height(16.dp))
            Text(text = stringResource(R.string.about_contact_content))
        }
    }
}
