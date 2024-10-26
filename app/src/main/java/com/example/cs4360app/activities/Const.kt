package com.example.cs4360app.activities

import android.content.Context
import com.example.cs4360app.R

@Suppress("ConstPropertyName")
class Const {
    companion object {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun getOpenWeatherMapApiKey(context: Context): String {
            return context.getString(R.string.open_weather_map)
        }

        const val colorBg2 = 0x4dff4dff
        const val colorBg3 = 0x00cc00
        const val cardColor = 0xFF333639
        const val LOADING = "Loading..."
        const val NA = "N/A"
    }
}