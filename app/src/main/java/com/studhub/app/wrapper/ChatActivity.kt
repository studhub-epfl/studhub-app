package com.studhub.app.wrapper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.studhub.app.domain.model.Conversation
import com.studhub.app.presentation.conversation.ChatScreen
import com.studhub.app.ui.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val conversationId = intent.getStringExtra(EXTRA_CONVERSATION_ID) ?: ""

        setContent {
            ChatScreen(
                conversation = Conversation(id = conversationId)
            )
        }
    }

    companion object {
        const val EXTRA_CONVERSATION_ID = "conversation_id"
    }
}
