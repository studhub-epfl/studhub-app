package com.studhub.app.presentation.listing.browse.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Preview
@Composable
fun SearchBar(
    search: MutableState<String> = remember { mutableStateOf("") },
    onSearch: () -> Unit = {}
) {
    var job: Job? = null
    val coroutineScope = rememberCoroutineScope()

    //delay in miliseconds used for dynamic searching as user types
    val searchDelay: Long = 800

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
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

            OutlinedTextField(
                value = search.value,
                //We want to allow a search when text is typed so it searches as soon as possible
                //but we don't want to send a query to each character change, rather, when the user
                //has stopped writing. cancelling the job on each change and starting a new one
                //with a delay achieves this result. A delay of 800ms felt good.
                onValueChange = {
                    search.value = it
                    job?.cancel()
                    job = coroutineScope.launch {
                        delay(searchDelay)
                        onSearch()
                    }
                },
                label = {
                    Text(
                        text = "Search...",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
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
                trailingIcon = {
                    if (search.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                search.value = ""
                                onSearch()
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear button",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        )
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        // Handle focus change
                    },
            )
        }
    }
}
