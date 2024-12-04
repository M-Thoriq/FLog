package com.thoriq.flog.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.thoriq.flog.R
import com.thoriq.flog.data.FishLocation

// Update MapsScreen to accept a Modifier parameter
@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double,
    jsonData: String

) {

    val context = LocalContext.current
    val markerIcon = BitmapFactory.decodeResource(context.resources, R.drawable.marker)
    val iconIkan = Bitmap.createScaledBitmap(markerIcon, 120, 145, false)
    var isMapLoaded by remember { mutableStateOf(false) }


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


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(horizontal = 0.dp, vertical = 6.dp)
                        .clip(shape = RoundedCornerShape(32.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var previousText by remember { mutableStateOf("") }

                    TextField(
                        value = searchQuery,
//                        colors = TextFieldColors(M),
                        onValueChange = { newText ->
                            if (newText.endsWith("\n")) {
                                matchingLocations = locationList.filter {
                                    it.title.contains(searchQuery.trim(), ignoreCase = true)
                                }
                                currentIndex = 0

                                if (matchingLocations.isNotEmpty()) {
                                    moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                                }
                                searchQuery = newText.trim()
                            } else {
                                searchQuery = newText
                            }
                            previousText = newText
                        },
                        placeholder = { Text("Search fish location...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 0.dp),
                    )

                    Button(
                        onClick = {
                            matchingLocations = locationList.filter {
                                it.title.contains(searchQuery, ignoreCase = true)
                            }
                            currentIndex = 0

                            if (matchingLocations.isNotEmpty()) {
                                moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                            }
                        },
                        modifier = Modifier
                            .heightIn(min = 56.dp),
                        shape = RectangleShape,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Black)
                    }
                }
            }

            if (matchingLocations.size > 1) {

                    Row(
                    ) {
                        Button(onClick = {
                            // Move to the previous marker
                            currentIndex =
                                if (currentIndex > 0) currentIndex - 1 else matchingLocations.size - 1
                            moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                        },
                            modifier = Modifier,
                            shape = RectangleShape

                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Search Icon")

                        }

                        HorizontalDivider(
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxHeight() // Match button height
                                .width(1.dp)
                                .padding(horizontal = 8.dp) // Optional padding around the divider
                        )

                        Button(onClick = {
                            // Move to the next marker
                            currentIndex = (currentIndex + 1) % matchingLocations.size
                            moveToLocation(matchingLocations[currentIndex], cameraPositionState)
                        },
                            modifier = Modifier,
                            shape = RectangleShape

                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Search Icon")

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



@Preview
@Composable
private fun MapsPrev() {
}