package com.thoriq.flog.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val Time: String,
    val Temp: String
) : Parcelable
