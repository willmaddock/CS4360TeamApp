package com.example.cs4360app.activities

data class LocationData(
    val name: String,             // Name of the garage or parking lot
    val imageResourceId: Int,     // Resource ID for the banner image
    val costBefore5pm: String,    // Cost information
    val recommendations: String   // Any recommendations or notes
)
