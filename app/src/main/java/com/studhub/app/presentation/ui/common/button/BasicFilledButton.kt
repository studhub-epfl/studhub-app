package com.studhub.app.presentation.ui.common.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicFilledButton(onClick: () -> Unit, label: String, enabled: Boolean = true) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp),
        enabled = enabled
    ) {
        Text(label)
    }
}
