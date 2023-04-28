package com.studhub.app.presentation.conversation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.studhub.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = messageText,
        onValueChange = { text -> messageText = text },
        label = { Text(stringResource(R.string.conversation_message_input_placeholder)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = {
            if (messageText.isNotBlank()) {
                onSend(messageText)
                messageText = ""
            }
        }),
        trailingIcon = {
            IconButton(onClick = {
                if (messageText.isNotBlank()) {
                    onSend(messageText)
                    messageText = ""
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = stringResource(R.string.conversation_message_input_send)
                )
            }
        }
    )
}