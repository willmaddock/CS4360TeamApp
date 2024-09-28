package com.example.cs4360app.activities

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs4360app.Constants
import com.example.cs4360app.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "YOUR_GOOGLE_AI_STUDIO_API_KEY",
    )

    fun sendMessage(question: String) {
       viewModelScope.launch {

           try{
               val chat = generativeModel.startChat(
                   history = messageList.map{
                       content (it.role){text(it.message)}
                   }.toList()
               )
               messageList.add(MessageModel(question, "User"))
               messageList.add(MessageModel("Generating response..", "Model"))

               val response = chat.sendMessage(question)
               messageList.removeLast()
               messageList.add(MessageModel(response.text.toString(), "Model"))
           } catch (e: Exception){
               messageList.removeLast()
               messageList.add(MessageModel("Error: "+e.message.toString(), "Model"))
           }

       }
    }
}