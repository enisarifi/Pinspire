package com.example.pinspire.ui.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessagesViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<String>>()
    val messages: LiveData<List<String>> = _messages

    init {
        _messages.value = listOf("Hello!", "Welcome to the chat") // Sample messages
    }

    fun addMessage(message: String) {
        val updatedMessages = _messages.value?.toMutableList() ?: mutableListOf()
        updatedMessages.add(message)
        _messages.value = updatedMessages
    }
}