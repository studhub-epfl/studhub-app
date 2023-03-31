package com.studhub.app.presentation.conversation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.conversation.components.ConversationItem
import com.studhub.app.presentation.ui.common.misc.LoadingCircle

@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel = hiltViewModel(),
    navigateToDiscussion: (id: String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getLoggedInUserConversations()
    }

    when (val conversations = viewModel.conversations) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            Surface {
                LazyColumn {
                    items(conversations.data) {
                        ConversationItem(it, onClick = { navigateToDiscussion(it.id) })
                    }
                }
            }
        }
    }
}