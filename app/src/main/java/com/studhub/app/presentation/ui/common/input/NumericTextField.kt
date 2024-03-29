package com.studhub.app.presentation.ui.common.input

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumericTextField(
    label: String,
    rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
) {
    OutlinedTextField(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        singleLine = true,
        value = rememberedValue.value,
        onValueChange = { rememberedValue.value = it },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedTextColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
