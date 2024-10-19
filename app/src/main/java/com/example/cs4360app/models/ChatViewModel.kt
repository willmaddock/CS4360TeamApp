package com.example.cs4360app.models

import android.content.Context
import android.icu.text.DateFormat
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs4360app.chatbots.Constants
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class ChatViewModel(private val context: Context) : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    private val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.getApiKey(context)  // Fetching API key from resources
    )

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val timestamp = DateFormat.getDateTimeInstance().format(Date())
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )
                messageList.add(MessageModel(question, "User", timestamp))

                // Save the user's message to shared preferences
                saveMessageToSharedPreferences(question, "User", timestamp)

                messageList.add(MessageModel("Generating response...", "Model", timestamp))

                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(MessageModel(response.text.toString(), "Model", timestamp))

                // Save the AI chat bot's response to shared preferences
                saveMessageToSharedPreferences(response.text.toString(), "Model", timestamp)
            } catch (e: Exception) {
                val timestamp = DateFormat.getDateTimeInstance().format(Date())
                messageList.removeLast()
                messageList.add(MessageModel("Error: " + e.message.toString(), "Model", timestamp))
                // Save the error message to shared preferences
                saveMessageToSharedPreferences("Error: " + e.message.toString(), "Model",timestamp)
            }
        }
    }

    private fun saveMessageToSharedPreferences(message: String, role: String, timestamp: String) {
        val sharedPreferences = context.getSharedPreferences("chat_history", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val currentChatHistory = sharedPreferences.getStringSet("chat_history", null)?.toMutableSet() ?: mutableSetOf()
        currentChatHistory.add("$role:$message:$timestamp")
        editor.putStringSet("chat_history", currentChatHistory)
        editor.apply()
    }
}