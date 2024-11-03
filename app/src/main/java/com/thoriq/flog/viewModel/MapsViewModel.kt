package com.thoriq.flog.viewModel

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.thoriq.flog.repository.WeatherRepository.Companion.latitude
import com.thoriq.flog.repository.WeatherRepository.Companion.longitude
import com.thoriq.flog.repository.WeatherRepository.Companion.mLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MapsViewModel {
}