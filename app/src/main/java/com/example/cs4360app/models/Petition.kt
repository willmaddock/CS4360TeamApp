package com.example.cs4360app.models

data class Petition(
    val userId: String,
    val description: String,
    val gracePeriod: String?,
    val timestamp: Long
)