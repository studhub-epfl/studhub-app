package com.studhub.app.wrapper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.studhub.app.presentation.conversation.ChatScreen
import com.studhub.app.presentation.conversation.ConversationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConversationScreen(navigateToDiscussion = {})
        }
    }
}