package com.studhub.app.presentation.conversation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message

@Composable
fun MessageList(conversation: Conversation, messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(conversation, message)
        }
    }
}
