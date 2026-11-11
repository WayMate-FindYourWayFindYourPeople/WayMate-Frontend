package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.ChatMessage
import com.waymate.app.data.model.Conversation
import com.waymate.app.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MessageUiState(
    val conversations: List<Conversation> = emptyList(),
    val activeConversation: Conversation? = null,
    val currentMessages: List<ChatMessage> = emptyList(),
    val typedMessage: String = ""
)

class MessageViewModel(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            messageRepository.conversations.collect { list ->
                _uiState.value = _uiState.value.copy(conversations = list)
            }
        }
    }

    fun openConversation(conversation: Conversation) {
        val messages = messageRepository.getMessagesForConversation(conversation.id)
        _uiState.value = _uiState.value.copy(
            activeConversation = conversation,
            currentMessages = messages,
            typedMessage = ""
        )
    }

    fun onTypedMessageChange(text: String) {
        _uiState.value = _uiState.value.copy(typedMessage = text)
    }

    fun sendMessage() {
        val conv = _uiState.value.activeConversation ?: return
        val text = _uiState.value.typedMessage.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            val newMsg = messageRepository.sendMessage(conv.id, text)
            _uiState.value = _uiState.value.copy(
                currentMessages = _uiState.value.currentMessages + newMsg,
                typedMessage = ""
            )
        }
    }
}
