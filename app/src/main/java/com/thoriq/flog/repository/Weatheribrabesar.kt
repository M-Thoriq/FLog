package com.thoriq.flog.repository

import com.thoriq.flog.data.Weatheribra

class Weatheribrabesar {
    fun getAllData(): List<Weatheribra>{
        return listOf(
            Weatheribra(
                time = "12:00",
                temperature = "25",
                image = "ic_launcher_background"
            ),
            Weatheribra(
                time = "13:00",
                temperature = "26",
                image = "ic_launcher_background"
            ),
            Weatheribra(
                time = "14:00",
                temperature = "27",
                image = "ic_launcher_background"
            ),
            Weatheribra(
                time = "15:00",
                temperature = "28",
                image = "ic_launcher_background"
            ),
            Weatheribra(
                time = "16:00",
                temperature = "29",
                image = "ic_launcher_background"
            )
        )
    }
}