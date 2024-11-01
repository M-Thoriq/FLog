package com.thoriq.flog.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thoriq.flog.R
import com.thoriq.flog.repository.WeatherPreviewRepository
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.thoriq.flog.RecentCatchItem
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.hours

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val weathers = remember { mutableStateOf<List<Weather>>(emptyList()) }
    val context = LocalContext.current
    val weatherRepository = WeatherRepository(context)
    var isWeatherLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        weatherRepository.fetchWeatherData(context) { weatherList ->
            weathers.value = weatherList
            isWeatherLoading = false
        }
    }
    val nowWeathers = getNowWeather(weathers.value)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        GreetingSection()

        val iconWeather = when (nowWeathers?.weatherCode) {
            0 -> R.drawable.sunny
            1, 2, 3 -> R.drawable.partly_cloudy_day
            45, 48 -> R.drawable.foggy
            51, 53, 55 -> R.drawable.drizzle
            56, 57 -> R.drawable.freezing_drizzle
            61, 63, 65 -> R.drawable.rainy
            66, 67 -> R.drawable.freezing_rain
            71, 73, 75 -> R.drawable.snowfall
            77 -> R.drawable.cloudy_snowing
            80, 81, 82 -> R.drawable.rain_showers
            85, 86 -> R.drawable.snow_showers
            95 -> R.drawable.thunderstorm
            else -> R.drawable.thunderstorm_hail
        }
        val weatherCondition = when (nowWeathers?.weatherCode) {
            0 -> "Sunny Day"
            1, 2, 3 -> "Cloudy Day"
            45, 48 -> "Foggy Day"
            51, 53, 55 -> "Drizzly Day"
            56, 57 -> "Freezing Drizzle"
            61, 63, 65 -> "Rainy Day"
            66, 67 -> "Freezing Rain"
            71, 73, 75 -> "Snowy Day"
            77 -> "Cloudy Snow"
            80, 81, 82 -> "Rain Shower"
            85, 86 -> "Snow Shower"
            95 -> "Thunderstorm"
            else -> "Heavy Thunderstorm"
        }
        if (!isWeatherLoading){
            WeatherCard(
                temperature = "${nowWeathers?.Temp}°C",
                condition = weatherCondition,
                location = "Medan",
                time = "${getLocalHour()} WIB",
                icon = iconWeather,
                weatherList = weathers.value
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator()
            }
        }
        RecentCatch()
    }
}

private fun getNowWeather(weathers: List<Weather>): Weather? {
    val currentHour = getLocalHour()
    return weathers.find { getHourFromTimeString(it.Time) == currentHour }
}

private fun getHourFromTimeString(timeString: String): Int {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val dateTime = LocalDateTime.parse(timeString, formatter)
    return dateTime.hour
}

private fun getLocalHour(): Int {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    return currentHour
}


@Composable
fun GreetingSection(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.akun), contentDescription = "Image Akun",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Text(text = "Good Morning", fontSize = 24.sp)
            Text(text = "Have a nice day!")
        }
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    temperature: String,
    condition: String,
    location: String,
    time: String,
    icon: Int,
    weatherList: List<Weather>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = temperature,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
//        val weatherPrev = WeatherPreviewRepository()
//        val getAllData = weatherPrev.getAllData()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(items = weatherList) { weatherPrev ->
                WeatherPreviewItem(weatherPreview = weatherPrev, icon)
            }
        }
    }
}

@Composable
fun RecentCatch(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 2.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = "Recent Catch",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "View All >",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Blue
        )
    }
    val recentCatch = com.thoriq.flog.repository.RecentCatchRepository()
    val getAllData = recentCatch.getAllData()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(items = getAllData) { recentCatch ->
            RecentCatchItem(recentCatch = recentCatch)
        }

    }
}

@Composable
fun WeatherPreviewItem(weatherPreview: Weather, icon: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${getHourFromTimeString(weatherPreview.Time)}:00 WIB",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Icon(painter = painterResource(icon), contentDescription = "",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
        )
        Text(text = weatherPreview.Temp,
            style = MaterialTheme.typography.labelLarge,
        )

    }
}
