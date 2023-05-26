package com.studhub.app.presentation.listing.add.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.studhub.app.R

/**
 * Composable responsible to render the price part of the form
 * @param rememberedValue the state holding the currently written price.
 */
@Composable
fun PriceRow(rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }) {
    Row(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(100.dp)
                .padding(end = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            value = rememberedValue.value,
            onValueChange = { rememberedValue.value = it },
            label = { Text(stringResource(R.string.listings_add_form_price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(stringResource(R.string.misc_currency_symbol))
    }
}
