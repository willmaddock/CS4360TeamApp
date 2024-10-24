package com.example.cs4360app.activities

class Const {
    companion object {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        const val openWeatherMap_API_KEY = "YOUR_OPEN_WEATHER_MAP_API_KEY"
        const val colorBg1 = 0xff08203e;
        const val colorBg2 = 0x4dff4dff;
        const val colorBg3 = 0x00cc00;
        const val cardColor = 0xFF333639;
        const val LOADING = "Loading...";
        const val NA = "N/A";
    }
}