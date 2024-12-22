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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import com.thoriq.flog.RecentCatchItem
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository
import com.thoriq.flog.ui.theme.blueTransparent
import com.thoriq.flog.viewModel.WeatherViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.hours

fun iconWeather(weatherCode : Int) : Int {
    return when (weatherCode) {
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
}

fun weatherCondition(weatherCode : Int) : String {
    return when (weatherCode) {
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
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel
) {
    val context = LocalContext.current

    val nowWeathers = getNowWeather(weatherViewModel.weatherList)
    val isLoaded: Flow<Boolean> = weatherViewModel.isLoaded

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        GreetingSection()


        if (isLoaded.collectAsState(initial = false).value) {
            WeatherCard(
                temperature = "${nowWeathers?.Temp}°C",
                condition = weatherCondition(nowWeathers?.weatherCode ?: 0),
                location = "Medan",
                time = "${getLocalHour()} WIB",
                icon = iconWeather(nowWeathers?.weatherCode ?: 0),
                weatherList = weatherViewModel.weatherList
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
                painter = painterResource(id = R.drawable._3f1eda09c78fa01cb765443993748ee), contentDescription = "Image Akun",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Text(text = "Welcome!", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
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
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.hd_bg),
            contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .size(374.dp)

        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Card(
                modifier = modifier

                    .fillMaxWidth()

                ,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ,

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
//        Text(
//            text = "View All >",
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Blue
//        )
    }
    val recentCatch = com.thoriq.flog.repository.RecentCatchRepository()
    val getAllData = recentCatch.getAllData()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
        Text(
            text = "${getHourFromTimeString(weatherPreview.Time)}:00 WIB",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Icon(
            painter = painterResource(iconWeather(weatherPreview.weatherCode)), contentDescription = "",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
        )
        Text(
            text = weatherPreview.Temp,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
