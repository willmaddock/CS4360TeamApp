package com.example.cs4360app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import coil.compose.AsyncImage
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

    /*
    Title Section
    */
    var title = ""
    if (!weatherResponse.name.isNullOrEmpty()) {
        weatherResponse?.name?.let {
            title = it
        }
    }
    else{
        weatherResponse.coord?.let {
            title = "${it.lat}, ${it.lon}"
        }
    }

    /*
    Subtitle Section
    */
    var subTitle = ""
    val dateVal = (weatherResponse.dt ?: 0)
    subTitle = if (dateVal == 0) LOADING
    else timestampToHumanDate(dateVal.toLong(), "MM/dd/yyyy")

    /*
    Temperature Section
    */
    var temp = ""
    weatherResponse.main?.let{
        temp = "${it.temp} °C"
    }

    /*
    Wind Section
    */
    var wind = ""
    weatherResponse.wind.let{
        wind = if (it == null) LOADING else "Wind: ${it.speed}"
    }

    /*
    Cloud Section
    */
    var clouds = ""
    weatherResponse.clouds.let{
        clouds = if (it == null) LOADING else "Clouds: ${it.all}"
    }

    /*
   Snow Section
   */
    var snow = ""
    weatherResponse.snow.let{
        snow = if (it!!.d1h == null) NA else "Snow: ${it.d1h}"
    }

    /*
    Weather Icon
     */
    var icon = ""
    var description = ""
    weatherResponse.weather.let{
        if (it!!.size > 0){
            description = if(it[0].description == null) LOADING else it[0].description!!
            icon = if(it[0].icon == null) LOADING else it[0].icon!!
        }
    }
    WeatherTitleSection(text = title, subText = subTitle, fontSize = 27.sp)
    WeatherImage(icon = icon)
    WeatherTitleSection(text = temp, subText = description, fontSize = 57.sp)
    Row(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    )
    {
        WeatherInfo(icon = FaIcons.Wind, text = wind)
        WeatherInfo(icon = FaIcons.Cloud, text = clouds)
        WeatherInfo(icon = FaIcons.Snowflake, text = snow)
    }
}

@Composable
fun WeatherInfo(icon: FaIconType.SolidIcon, text:String) {
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
