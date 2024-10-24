package com.example.cs4360app.api

import com.example.cs4360app.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService {
    @GET("events.json")
    fun getEventsByVenue(
        @Query("venueId") venueId: String,
        @Query("apikey") apiKey: String
    ): Call<EventResponse>
}