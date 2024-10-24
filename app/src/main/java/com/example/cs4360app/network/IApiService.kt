package com.example.cs4360app.network

import com.example.cs4360app.activities.Const.Companion.openWeatherMap_API_KEY
import com.example.cs4360app.models.forecast.ForecastResult
import com.example.cs4360app.models.weather.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lng: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = openWeatherMap_API_KEY
    ): WeatherResult

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lng: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = openWeatherMap_API_KEY
    ): ForecastResult
}