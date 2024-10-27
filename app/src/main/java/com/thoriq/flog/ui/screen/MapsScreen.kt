package com.thoriq.flog.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.thoriq.flog.R
import com.thoriq.flog.data.FishLocation
import com.thoriq.flog.repository.WeatherRepository





// Update MapsScreen to accept a Modifier parameter
@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    latitude: Double,
    longitude: Double

) {

    val context = LocalContext.current
    val markerIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ikan2)
    val iconIkan = Bitmap.createScaledBitmap(markerIcon, 120, 120, false)
    var isMapLoaded by remember { mutableStateOf(false) }

    val jsonData ="[{'title':'Fish 1','snippet':'Fish 1','latitude':3.585,'longitude':98.6656},{'title':'Fish 2','snippet':'Fish 2','latitude':3.5839,'longitude':98.67}]"


    val locationList: List<FishLocation> = remember {
        val type = object : TypeToken<List<FishLocation>>() {}.type
        Gson().fromJson(jsonData, type)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(3.583, 98.666), 16f) // 12f is the zoom level
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Add GoogleMap here
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true },


        ){
            // Add Marker at the given latitude and longitude
            Marker(
                state = rememberMarkerState(position = LatLng(3.583, 98.666)),
                title = "Your Location",
                snippet = "This is a marker at your specified location."
            )
            locationList.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = location.title,
                    snippet = location.snippet,
                    icon = BitmapDescriptorFactory.fromBitmap(iconIkan)
                )
            }
        }

        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentSize()
                )
            }
        }
    }
}



//@Preview
//@Composable
//private fun MapsPrev() {
//    MapsScreen(paddingValues = PaddingValues(0.dp))
//}