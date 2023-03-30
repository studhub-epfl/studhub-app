package com.studhub.app.presentation.conversation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.ui.chat.ChatViewModel
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel(), conversation: Conversation) {
    val messages by viewModel.messages.collectAsState()

    viewModel.loadMessages(conversation)

    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            MessageList(messages = messages)
            MessageInput(onSend = { message -> viewModel.sendMessage(conversation, message) })
        }
    }
}

@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Composable
fun MessageCard(message: Message) {
    // Display the message content
    Text(text = message.content)
}

@Composable
fun MessageInput(onSend: (Message) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { text -> messageText = text },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                if (messageText.isNotBlank()) {
                    onSend(Message(content = messageText))
                    messageText = ""
                }
            })
        )

        TextButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSend(Message(content = messageText))
                    messageText = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Send")
        }
    }
}




