package com.studhub.app.presentation.ui.common.input

import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTextField(
    label: String,
    rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
) {
    OutlinedTextField(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        singleLine = true,
        value = rememberedValue.value,
        onValueChange = { rememberedValue.value = it },
        label = { Text(label) }
    )
}
