package com.example.cs4360app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cs4360app.activities.Const.Companion.NA
import com.example.cs4360app.activities.Const.Companion.cardColor
import com.example.cs4360app.models.forecast.ForecastResult
import com.example.cs4360app.utils.Utils.Companion.buildIcon
import com.example.cs4360app.utils.Utils.Companion.timestampToHumanDate

@Composable
fun ForecastSection(forecastResponse: ForecastResult) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        forecastResponse.list?.let { listForecast ->
            if (listForecast.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listForecast) { currentItem ->
                        currentItem.let { item ->
                            var temp = ""
                            var icon = ""
                            var time = ""

                            item.main?.let { main ->
                                temp = "${main.temp}°C"
                            }

                            item.weather?.let { weather ->
                                icon = weather[0].icon ?: NA
                            }

                            item.dt?.let { dateTime ->
                                time = timestampToHumanDate(dateTime.toLong(), "EEE HH:mm")
                            }

                            ForecastTitle(temp = temp, image = icon, time = time)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastTitle(temp: String, image: String, time: String) {
    val iconResId = buildIcon(image)
    Card(
        modifier = Modifier.padding(20.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(cardColor).copy(alpha = 0.7f),
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(72.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = temp.ifEmpty { NA }, color = Color.Black)
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = image,
                modifier = Modifier.width(50.dp).height(50.dp),
                contentScale = ContentScale.FillBounds
            )
            Text(text = time.ifEmpty { NA }, color = Color.Black)
        }
    }
}