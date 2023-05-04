package com.studhub.app.ui.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.usecase.conversation.GetConversation
import com.studhub.app.domain.usecase.conversation.GetConversationMessages
import com.studhub.app.domain.usecase.conversation.SendMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
 class ChatViewModel @Inject constructor(
    private val getConversationMessages: GetConversationMessages,
    private val getConversation: GetConversation,
    private val _sendMessage: SendMessage,
) : ViewModel() {
    private val _messages = MutableStateFlow<ApiResponse<List<Message>>>(ApiResponse.Loading)
    val messages: StateFlow<ApiResponse<List<Message>>> = _messages


    var conversation by mutableStateOf<Conversation?>(null)
        private set

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            getConversation(conversationId).collect {
                if (it is ApiResponse.Success<Conversation>) {
                    conversation = it.data
                    getConversationMessages(it.data).collect { response ->
                        _messages.value = response
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        if (conversation == null) {
            return
        }

        viewModelScope.launch {
            _sendMessage(conversation!!, Message(content = message)).collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                    is ApiResponse.Failure -> {
                        Log.e("TAG", it.message)
                    }
                }
            }
        }
    }


}
