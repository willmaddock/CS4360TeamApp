package com.example.cs4360app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.cs4360app.activities.ChatViewModel

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
            ChatPage(Modifier, chatViewModel)
        }
    }
}