package com.studhub.app.presentation.conversation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.domain.model.Conversation
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(8.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation()

    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column {
                Row {
                    Text(
                        text = conversation.user2Name.ifEmpty { stringResource(R.string.conversation_unknown_user) },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (conversation.lastMessageContent.isNotEmpty()) {
                    Row {
                        Text(
                            shortenText(
                                conversation.lastMessageContent,
                                ellipsis = stringResource(R.string.misc_ellipsis)
                            )
                        )
                    }
                    Row {
                        Text(
                            text = String.format(
                                stringResource(R.string.conversation_last_message_at),
                                formatTime(conversation.updatedAt)
                            ),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
private fun formatTime(date: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = date

    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
    return formatter.format(calendar.time)
}

private fun shortenText(text: String, limit: Int = 40, ellipsis: String = ""): String {
    return text.take(limit) + if (text.length > limit) ellipsis else ""
}


@ExcludeFromGeneratedTestCoverage
@Preview
@Composable
fun ConversationItemPreview() {
    ConversationItem(
        conversation = Conversation(
            user2Id = "290470239",
            lastMessageContent = "Hello  my guy, how much for this? Also another question, what about that???"
        )
    )
}
