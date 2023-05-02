package com.studhub.app.presentation.ui.common.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RangeBar(label: String = "",
    search: MutableState<String> = rememberSaveable { mutableStateOf("") } ,
    onSearch: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .width(250.dp)
            .height(60.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
        ) {
            IconButton(
                onClick = {  }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Menu button",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            textField(label = label,
                              search = search,
                              onSearch = onSearch
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textField(label: String = "",
                      search: MutableState<String> = remember { mutableStateOf("") } ,
                      onSearch: () -> Unit = {}) {
    OutlinedTextField(
        value = search.value,
        onValueChange = { search.value = it },
        label = {
            Text(
                text = label, //"MIN....CHF",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        textStyle = MaterialTheme.typography.bodyLarge,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = { rangeBarTrailingIcon(search)},
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            cursorColor = MaterialTheme.colorScheme.onSurface,
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                // Handle focus change
            },
    )
}


@Composable
fun rangeBarTrailingIcon(search: MutableState<String> = remember { mutableStateOf("") }) {
    if (search.value.isNotEmpty()) {
        IconButton(
            onClick = { search.value = "" },
            content = {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear button",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        )
    } else {
        null
    }
}
