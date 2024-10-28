package com.thoriq.flog.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
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

    val jsonData ="[{'title':'Salmon','snippet':'Fish 1','latitude':3.885,'longitude':98.6656},{'title':'salmon','snippet':'Fish 2','latitude':3.5839,'longitude':98.67},{'title':'ikan salmon','snippet':'Fish 2','latitude':3.5877,'longitude':98.66},{'title':'hiu','snippet':'Fish 2','latitude':3.5739,'longitude':98.57},{'title':'hiu','snippet':'Fish 2','latitude':3.5849,'longitude':98.77}]"


    val locationList: List<FishLocation> = remember {
        val type = object : TypeToken<List<FishLocation>>() {}.type
        Gson().fromJson(jsonData, type)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(3.583, 98.666), 16f)
    }
    var searchQuery by remember { mutableStateOf("") }
    var matchingLocations by remember { mutableStateOf<List<FishLocation>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Add GoogleMap here
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isMapLoaded = true },
            ) {
                // Add Marker at the given latitude and longitude
                Marker(
                    state = rememberMarkerState(position = LatLng(3.583, 98.666)),
                    title = "Your Location",
                    snippet = "This is a marker at your specified location."
                )

                // Add other markers from locationList
                locationList.forEach { location ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(location.latitude, location.longitude)),
                        title = location.title,
                        snippet = location.snippet,
                        icon = BitmapDescriptorFactory.fromBitmap(iconIkan)
                    )
                }
            }

            // Overlay Row on top of the GoogleMap
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Search Bar Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("toriiq cobak ketik salmon") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    // Search Button
                    Button(onClick = {
                        // Filter locations matching the search query
                        matchingLocations = locationList.filter {
                            it.title.contains(searchQuery, ignoreCase = true)
                        }
                        currentIndex = 0 // Start with the first result if matches are found

                        // Move camera to the first matching location if any
                        if (matchingLocations.isNotEmpty()) {
                            moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                        }
                    }) {
                        Text("Search")
                    }
                }
                if (matchingLocations.size > 1) {
                    Spacer(modifier = Modifier.height(8.dp)) // Space between search bar and navigation buttons

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            // Move to the previous marker
                            currentIndex =
                                if (currentIndex > 0) currentIndex - 1 else matchingLocations.size - 1
                            moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                        }) {
                            Text("<-")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            // Move to the next marker
                            currentIndex = (currentIndex + 1) % matchingLocations.size
                            moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                        }) {
                            Text("->")
                        }
                    }
                }
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

private fun moveToLocation(location: FishLocation, cameraPositionState: CameraPositionState) {
    cameraPositionState.move(
        CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude, location.longitude),
            16f // Adjust zoom level as needed
        )
    )
}



//@Preview
//@Composable
//private fun MapsPrev() {
//    MapsScreen(paddingValues = PaddingValues(0.dp))
//}