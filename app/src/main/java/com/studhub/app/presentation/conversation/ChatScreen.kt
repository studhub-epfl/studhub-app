package com.studhub.app.presentation.conversation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.ui.chat.ChatViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    conversationId: String,
    navigateBack: () -> Unit
) {


    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId)
    }

    when (val messages = viewModel.messages.collectAsState().value) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            Scaffold(
                topBar = {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        BasicFilledButton(onClick = navigateBack, label = "<")
                        BigLabel(label = viewModel.conversation!!.user2!!.userName)
                    }
                },
                content = {
                    Column(modifier = Modifier.padding(it)) {
                        MessageList(conversation = viewModel.conversation!!, messages = messages.data)
                    }
                },
                bottomBar = { MessageInput(onSend = { message -> viewModel.sendMessage(message) }) }
            )
        }
    }
}

@Composable
fun MessageList(conversation: Conversation, messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(conversation, message)
        }
    }
}

@Composable
fun MessageCard(conversation: Conversation, message: Message) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .fillMaxWidth(0.75F)
                .align(if (message.senderId == conversation.user1Id) Alignment.CenterEnd else Alignment.CenterStart)
        ) {
            Text(modifier = Modifier.padding(16.dp), text = message.content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = messageText,
        onValueChange = { text -> messageText = text },
        label = { Text("Message") },
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
                Icon(imageVector = Icons.Filled.Send, contentDescription = "Send message")
            }
        }
    )
}




