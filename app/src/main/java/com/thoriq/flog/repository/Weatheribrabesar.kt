package com.thoriq.flog.repository

import com.thoriq.flog.R
import com.thoriq.flog.data.Weatheribra

class Weatheribrabesar {
    fun getAllData(): List<Weatheribra>{
        return listOf(
            Weatheribra(
                time = "10 AM",
                temperature = "25" + "°C",
                image = R.drawable.foggy
            ),
            Weatheribra(
                time = "11 AM",
                temperature = "26" + "°C",
                image = R.drawable.sunny
            ),
            Weatheribra(
                time = "12 PM",
                temperature = "27" + "°C",
                image = R.drawable.thunderstorm
            ),
            Weatheribra(
                time = "1 PM",
                temperature = "28" + "°C",
                image = R.drawable.partly_cloudy_day
            ),
            Weatheribra(
                time = "2 PM",
                temperature = "29" + "°C",
                image = R.drawable.thunderstorm_hail
            )
        )
    }
}