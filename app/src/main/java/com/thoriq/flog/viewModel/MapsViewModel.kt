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
//
//    private suspend fun getLatandLong() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        getLastLocation()
//    }
//
//    private suspend fun getLastLocation() = suspendCancellableCoroutine<Location?> { cont ->
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            cont.resume(null)
//        } else {
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location: Location? ->
//                    mLocation = location
//                    if (location != null) {
//                        latitude = location.latitude
//                        longitude = location.longitude
//                        Log.d("GMS", "getLastLocation: ${location.latitude} and ${location.longitude}")
//                    } else {
//                        latitude = 3.58333
//                        longitude = 98.66667
//                    }
//                    cont.resume(location)
//                }
//                .addOnFailureListener { e ->
//                    cont.resume(null)
//                }
//        }
//    }

}