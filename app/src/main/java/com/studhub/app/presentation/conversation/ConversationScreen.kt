package com.studhub.app.presentation.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.presentation.conversation.components.ConversationItem
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel = hiltViewModel(),
    navigateToDiscussion: (id: String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getLoggedInUserConversations()
    }

    Column {
        BigLabel(label = stringResource(R.string.conversation_title))
        
        when (val conversations = viewModel.conversations) {
            is ApiResponse.Loading -> LoadingCircle()
            is ApiResponse.Failure -> {}
            is ApiResponse.Success -> {
                LazyColumn {
                    items(conversations.data) {
                        ConversationItem(it, onClick = { navigateToDiscussion(it.id) })
                    }
                }
            }
        }
    }
}