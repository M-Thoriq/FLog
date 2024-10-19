package com.thoriq.flog.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier
) {
    val weathers = remember { mutableStateOf<List<Weather>>(emptyList()) }
    val context = LocalContext.current
    val weatherRepository = WeatherRepository(context)

    LaunchedEffect(Unit) {
        weatherRepository.fetchWeatherData(context) { weatherList ->
            weathers.value = weatherList
        }
    }

    WeathersCard(Weathers = weathers.value, modifier = modifier)
}


@Composable
fun WeathersCard(modifier: Modifier = Modifier, Weathers: List<Weather>) {
    LazyColumn(modifier = modifier) {
        items(Weathers) {
            Column {
                Text(text = "Time: ${it.Time}", color = Color.Black)
                Text(text = "Temperature: ${it.Temp}")
            }

        }
    }
}

@Preview
@Composable
fun WeatherPrev(){
    WeatherScreen()
}