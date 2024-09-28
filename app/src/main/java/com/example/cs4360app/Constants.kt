package com.example.cs4360app

import android.content.Context

object Constants {
    // Method to get API key from resources
    fun getApiKey(context: Context): String {
        return context.getString(R.string.google_app_key)
    }
}