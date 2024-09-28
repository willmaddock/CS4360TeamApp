package com.example.cs4360app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cs4360app.models.ChatViewModel

class ChatViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}