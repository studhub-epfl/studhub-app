package com.studhub.app.presentation.ui.common.button

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicFilledButton(onClick: () -> Unit, label: String, enabled: Boolean = true) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(4.dp), // Use a rounded rectangle shape with custom corner radius
        modifier = Modifier.padding(8.dp),
        enabled = enabled
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
