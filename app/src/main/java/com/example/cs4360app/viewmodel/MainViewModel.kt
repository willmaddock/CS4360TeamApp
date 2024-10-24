package com.example.cs4360app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs4360app.models.MyLatLng
import com.example.cs4360app.models.forecast.ForecastResult
import com.example.cs4360app.models.weather.WeatherResult
import com.example.cs4360app.network.RetrofitClient
import kotlinx.coroutines.launch

enum class STATE{
    LOADING,
    SUCCESS,
    FAILED
}

class MainViewModel: ViewModel() {
    var state by mutableStateOf(STATE.LOADING)
    var weatherResponse : WeatherResult by mutableStateOf(WeatherResult())
    var forecastResponse : ForecastResult by mutableStateOf(ForecastResult())
    var errorMessage : String by mutableStateOf("")

    fun getWeatherByLocation(latLng: MyLatLng)
    {
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstace()
            try {
                val apiResponse = apiService.getWeather(latLng.lat, latLng.lng)
                weatherResponse = apiResponse
                state = STATE.SUCCESS
            } catch (e: Exception) {
                errorMessage = e.message!!.toString()
                state = STATE.FAILED
            }
        }
    }

    fun getForecastByLocation(latLng: MyLatLng)
    {
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstace()
            try {
                val apiResponse = apiService.getForecast(latLng.lat, latLng.lng)
                forecastResponse = apiResponse
                state = STATE.SUCCESS
            } catch (e: Exception) {
                errorMessage = e.message!!.toString()
                state = STATE.FAILED
            }
        }
    }
}