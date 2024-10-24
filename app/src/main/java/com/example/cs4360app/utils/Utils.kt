package com.example.cs4360app.utils

import com.example.cs4360app.R
import java.text.SimpleDateFormat

class Utils {
    companion object
    {
        private val customIconMap = mapOf(
            "01d" to R.drawable.weather_icon4,
            "02d" to R.drawable.weather_icon1,
            "03d" to R.drawable.weather_icon8,
            "04d" to R.drawable.weather_icon5,
            "09d" to R.drawable.weather_icon12,
            "10d" to R.drawable.weather_icon7,
            "11d" to R.drawable.weather_icon2,
            "13d" to R.drawable.weather_icon3,
            "50d" to R.drawable.weather_icon11,
            "01n" to R.drawable.weather_icon13,
            "02n" to R.drawable.weather_icon6,
            "03n" to R.drawable.weather_icon9,
            "04n" to R.drawable.weather_icon14,
            "09n" to R.drawable.weather_icon16,
            "10n" to R.drawable.weather_icon15,
            "11n" to R.drawable.weather_icon10,
        )

        fun buildIcon(icon: String): Int {
            return customIconMap[icon] ?: R.drawable.weather_icon4
        }

        fun timestampToHumanDate(timestamp: Long, format: String): String {
            val sdf = SimpleDateFormat(format)
            return sdf.format(timestamp * 1000)
        }

    }
}