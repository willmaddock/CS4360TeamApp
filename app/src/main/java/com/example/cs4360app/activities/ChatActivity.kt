package com.example.cs4360app.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.cs4360app.ChatPage
import com.example.cs4360app.ChatViewModelFactory
import com.example.cs4360app.models.ChatViewModel

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val chatViewModel = ViewModelProvider(
                this,
                ChatViewModelFactory(applicationContext)
            )[ChatViewModel::class.java]

            ChatPage(Modifier, chatViewModel)
        }
    }
}