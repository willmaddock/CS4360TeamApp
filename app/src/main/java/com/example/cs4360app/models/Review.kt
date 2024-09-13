package com.example.cs4360app.models

data class Review(
    val userId: String = "",            // The ID of the user who submitted the review
    val parkingLotId: String = "",      // The ID of the parking lot being reviewed
    val rating: Float = 0f,             // Rating out of 5 stars
    val comment: String = "",           // User's comment about the parking lot
    val timestamp: Long = System.currentTimeMillis()  // Time the review was submitted
)