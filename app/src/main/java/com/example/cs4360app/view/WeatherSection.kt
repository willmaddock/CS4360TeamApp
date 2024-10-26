package com.example.cs4360app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs4360app.activities.Const.Companion.LOADING
import com.example.cs4360app.activities.Const.Companion.NA
import com.example.cs4360app.models.weather.WeatherResult
import com.example.cs4360app.utils.Utils.Companion.buildIcon
import com.example.cs4360app.utils.Utils.Companion.timestampToHumanDate
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun WeatherSection(weatherResponse: WeatherResult) {
    var title = ""
    if (!weatherResponse.name.isNullOrEmpty()) {
        title = weatherResponse.name ?: ""
    } else {
        weatherResponse.coord?.let {
            title = "${it.lat}, ${it.lon}"
        }
    }

    val dateVal = (weatherResponse.dt ?: 0)
    val subTitle = if (dateVal == 0) LOADING else timestampToHumanDate(dateVal.toLong(), "MM/dd/yyyy hh:mm a")

    var temp = ""
    weatherResponse.main?.temp?.let { celsiusTemp ->
        val fahrenheitTemp = (celsiusTemp * 9 / 5) + 32
        temp = "${fahrenheitTemp.toInt()} °F"
    } ?: run {
        temp = NA
    }

    var wind = ""
    weatherResponse.wind.let {
        wind = if (it == null) LOADING else "Wind: ${it.speed} mph"
    }

    var clouds = ""
    weatherResponse.clouds.let {
        clouds = if (it == null) LOADING else "Clouds: ${it.all}%"
    }

    var snow = ""
    weatherResponse.snow.let {
        snow = if (it?.d1h == null) NA else "Snow: ${it.d1h} mm"
    }

    var icon = ""
    var description = ""
    weatherResponse.weather.let {
        if (!it.isNullOrEmpty()) {
            description = it[0].description ?: LOADING
            icon = it[0].icon ?: LOADING
        }
    }

    WeatherTitleSection(text = title, subText = subTitle, fontSize = 27.sp)
    WeatherImage(icon = icon)
    WeatherTitleSection(text = temp, subText = description, fontSize = 57.sp)
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeatherInfo(icon = FaIcons.Wind, text = wind)
        WeatherInfo(icon = FaIcons.Cloud, text = clouds)
        WeatherInfo(icon = FaIcons.Snowflake, text = snow)
    }
}

@Composable
fun WeatherInfo(icon: FaIconType.SolidIcon, text: String) {
    Column {
        FaIcon(faIcon = icon, size = 48.dp, tint = Color.Black)
        Text(text, fontSize = 24.sp, color = Color.Black)
    }
}

@Composable
fun WeatherImage(icon: String) {
    val iconResId = buildIcon(icon)
    Image(
        painter = painterResource(id = iconResId),
        contentDescription = icon,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun WeatherTitleSection(text: String, subText: String, fontSize: TextUnit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text, fontSize = fontSize, color = Color.Black, fontWeight = FontWeight.Bold)
        Text(subText, fontSize = 17.sp, color = Color.Black)
    }
}