package com.studhub.app.presentation.conversation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.conversation.GetCurrentUserConversations
import com.studhub.app.domain.usecase.user.GetCurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val getCurrentUser: GetCurrentUser,
    private val getCurrentUserConversations: GetCurrentUserConversations
) : ViewModel() {
    var currentUser by mutableStateOf<ApiResponse<User>>(ApiResponse.Loading)
        private set

    var conversations by mutableStateOf<ApiResponse<List<Conversation>>>(ApiResponse.Loading)
        private set

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() =
        viewModelScope.launch {
            getCurrentUser().collect {
                currentUser = it
            }
        }

    fun getLoggedInUserConversations() =
        viewModelScope.launch {
            getCurrentUserConversations().collect {
                conversations = it
            }
        }
}
