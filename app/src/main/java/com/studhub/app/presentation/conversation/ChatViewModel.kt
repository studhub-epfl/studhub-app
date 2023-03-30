package com.studhub.app.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.usecase.conversation.GetConversationMessages
import com.studhub.app.domain.usecase.conversation.SendMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
 class ChatViewModel @Inject constructor(
    private val getConversationMessages: GetConversationMessages,
    private val sendMessage: SendMessage,
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    open val messages: StateFlow<List<Message>> = _messages

    fun loadMessages(conversation: Conversation) {
        viewModelScope.launch {
            getConversationMessages(conversation).collect { response ->
                if (response is ApiResponse.Success<List<Message>>) {
                    _messages.value = response.data
                }
            }
        }
    }

    fun sendMessage(conversation: Conversation, message: Message) {
        viewModelScope.launch {
            val response = sendMessage.invoke(conversation, message).first()

            if (response is ApiResponse.Success<Message>) {
                val updatedMessages = _messages.value.toMutableList()
                updatedMessages.add(response.data)
                _messages.value = updatedMessages
            }
        }
    }


}
