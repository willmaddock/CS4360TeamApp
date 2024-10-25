package com.example.cs4360app.models

data class EventResponse(
    val events: List<Event>
)

data class Event(
    val name: String,
    val dates: Dates,
    val url: String
)

data class Dates(
    val start: Start
)

data class Start(
    val localDate: String
)