package com.studhub.app.presentation.ui.common.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchBar(
    search: MutableState<String> = remember {
        mutableStateOf("")
    }, onSearch: () -> Unit = {}
) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Handle menu button click */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu button"
                )
            }
            TextField(
                value = search.value,
                onValueChange = { search.value = it },
                textStyle = MaterialTheme.typography.bodyLarge,
                label = {
                    Text(
                        text = "Search...",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.LightGray)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()

                    }
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search icon"
                    )
                },
                trailingIcon = {
                    if (search.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                search.value = ""
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear button"
                                )
                            }
                        )
                    } else {
                        null
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    textColor = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1,
                shape = MaterialTheme.shapes.small,
                //onLeadingIconClick = {
                // Handle leading icon click
                //},
                interactionSource = remember { MutableInteractionSource() },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        // Handle focus change
                    }
            )


        }
    }
}

