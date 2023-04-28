package com.studhub.app.presentation.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message

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
