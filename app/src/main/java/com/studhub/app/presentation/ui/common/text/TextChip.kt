package com.studhub.app.presentation.ui.common.text

import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextChip(
    onClick: () -> Unit = {},
    label: String, trailingIcon: @Composable (() -> Unit)? = null
) {
    ElevatedAssistChip(
        onClick = { onClick() },
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        trailingIcon = trailingIcon
    )
}
