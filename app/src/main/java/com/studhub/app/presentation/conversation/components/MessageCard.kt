package com.studhub.app.presentation.conversation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.presentation.ui.common.container.Card

@Composable
fun MessageCard(conversation: Conversation, message: Message) {
    val alignment: Alignment
    val containerColor: Color
    val shape: Shape
    val radius = 16.dp

    // user1 is ensured to be the logged-in user
    if (message.senderId == conversation.user1Id) {
        alignment = Alignment.CenterEnd
        containerColor = MaterialTheme.colorScheme.primaryContainer
        shape = RoundedCornerShape(topStart = radius, topEnd = radius, bottomStart = radius)
    } else {
        alignment = Alignment.CenterStart
        containerColor = MaterialTheme.colorScheme.secondaryContainer
        shape = RoundedCornerShape(topStart = radius, topEnd = radius, bottomEnd = radius)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.75F)
                .align(alignment)
                .clip(shape),
            containerColor = containerColor,
        ) {
            Text(modifier = Modifier.padding(16.dp), text = message.content)
        }
    }
}
