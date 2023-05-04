package com.studhub.app.presentation.conversation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.conversation.components.ChatTopBar
import com.studhub.app.presentation.conversation.components.MessageInput
import com.studhub.app.presentation.conversation.components.MessageList
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
                    ChatTopBar(
                        correspondentName = viewModel.conversation!!.user2Name,
                        navigateBack = navigateBack
                    )
                },
                content = {
                    Column(modifier = Modifier.padding(it)) {
                        MessageList(
                            conversation = viewModel.conversation!!,
                            messages = messages.data
                        )
                    }
                },
                bottomBar = { MessageInput(onSend = { message -> viewModel.sendMessage(message) }) }
            )
        }
    }
}
