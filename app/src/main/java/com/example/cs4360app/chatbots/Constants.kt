package com.example.cs4360app.chatbots

import android.content.Context
import com.example.cs4360app.R

object Constants {
    // Method to get API key from resources
    fun getApiKey(context: Context): String {
        return context.getString(R.string.google_app_key)
    }
}