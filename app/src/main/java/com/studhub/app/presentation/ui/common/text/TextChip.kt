package com.studhub.app.presentation.ui.common.text

import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextChip(onClick : () -> Unit = {}, label: String) {
    AssistChip(onClick = { onClick() },
        label = {
            Text(label)
        })
}
