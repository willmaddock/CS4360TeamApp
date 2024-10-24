package com.example.cs4360app.models.forecast

import com.example.cs4360app.models.weather.Clouds
import com.example.cs4360app.models.weather.Coord
import com.example.cs4360app.models.weather.Snow
import com.example.cs4360app.models.weather.Wind
import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("coord") var coord: Coord? = Coord(),
    @SerializedName("country") var country: String? = null,
    @SerializedName("population") var population: Int? = null,
    @SerializedName("timezone") var timezone: Int? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
)
