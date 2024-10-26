package com.example.cs4360app.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs4360app.activities.Const
import com.example.cs4360app.models.MyLatLng
import com.example.cs4360app.models.forecast.ForecastResult
import com.example.cs4360app.models.weather.WeatherResult
import com.example.cs4360app.network.RetrofitClient
import kotlinx.coroutines.launch

enum class STATE {
    LOADING,
    SUCCESS,
    FAILED
}

class MainViewModel @SuppressLint("StaticFieldLeak") constructor(private val context: Context) : ViewModel() {
    var state by mutableStateOf(STATE.LOADING)
    var weatherResponse: WeatherResult by mutableStateOf(WeatherResult())
    var forecastResponse: ForecastResult by mutableStateOf(ForecastResult())
    var errorMessage: String by mutableStateOf("")

    private val apiKey = Const.getOpenWeatherMapApiKey(context) // Get the API key

    fun getWeatherByLocation(latLng: MyLatLng) {
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance() // Correct method name
            try {
                val apiResponse = apiService.getWeather(latLng.lat, latLng.lng, "metric", apiKey)
                weatherResponse = apiResponse
                state = STATE.SUCCESS
            } catch (e: Exception) {
                errorMessage = e.message!!.toString()
                state = STATE.FAILED
            }
        }
    }

    fun getForecastByLocation(latLng: MyLatLng) {
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance() // Correct method name
            try {
                val apiResponse = apiService.getForecast(latLng.lat, latLng.lng, "metric", apiKey)
                forecastResponse = apiResponse
                state = STATE.SUCCESS
            } catch (e: Exception) {
                errorMessage = e.message!!.toString()
                state = STATE.FAILED
            }
        }
    }
}