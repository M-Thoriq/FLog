package com.thoriq.flog.repository

import com.thoriq.flog.R
import com.thoriq.flog.data.WeatherPreview

class WeatherPreviewRepository {
    fun getAllData(): List<WeatherPreview>{
        return listOf(
            WeatherPreview(
                time = "10 AM",
                temperature = "25" + "°C",
                image = R.drawable.foggy
            ),
            WeatherPreview(
                time = "11 AM",
                temperature = "26" + "°C",
                image = R.drawable.sunny
            ),
            WeatherPreview(
                time = "12 PM",
                temperature = "27" + "°C",
                image = R.drawable.thunderstorm
            ),
            WeatherPreview(
                time = "1 PM",
                temperature = "28" + "°C",
                image = R.drawable.partly_cloudy_day
            ),
            WeatherPreview(
                time = "2 PM",
                temperature = "29" + "°C",
                image = R.drawable.thunderstorm_hail
            ),WeatherPreview(
                time = "3 PM",
                temperature = "25" + "°C",
                image = R.drawable.foggy
            ),
            WeatherPreview(
                time = "4 PM",
                temperature = "26" + "°C",
                image = R.drawable.sunny
            ),
            WeatherPreview(
                time = "5 PM",
                temperature = "27" + "°C",
                image = R.drawable.thunderstorm
            ),
            WeatherPreview(
                time = "6 PM",
                temperature = "28" + "°C",
                image = R.drawable.partly_cloudy_day
            ),
            WeatherPreview(
                time = "7 PM",
                temperature = "29" + "°C",
                image = R.drawable.thunderstorm_hail
            ),WeatherPreview(
                time = "8 PM",
                temperature = "28" + "°C",
                image = R.drawable.cloudy_snowing
            ),
            WeatherPreview(
                time = "9 PM",
                temperature = "29" + "°C",
                image = R.drawable.snowfall
            )

        )
    }
}