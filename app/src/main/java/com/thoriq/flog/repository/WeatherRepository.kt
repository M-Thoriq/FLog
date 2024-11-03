package com.thoriq.flog.repository

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.thoriq.flog.config.STATE
import com.thoriq.flog.data.Weather
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class WeatherRepository(private var context: Context) {

    companion object  {
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var mLocation: Location? = null
    }

    private val requestPermissionCode = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun saveWeatherData(context: Context, weatherList: List<Weather>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonArray = JSONArray()
        for (weather in weatherList) {
            val weatherObject = JSONObject().apply {
                put("time", weather.Time)
                put("temp", weather.Temp)
                put("weather_code", weather.weatherCode)
            }
            jsonArray.put(weatherObject)
        }

        editor.putString("weather_data", jsonArray.toString())
        editor.apply()
    }

    private suspend fun getLatandLong() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        getLastLocation()
    }

    private suspend fun getLastLocation() = suspendCancellableCoroutine<Location?> { cont ->
        if (ActivityCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            cont.resume(null)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                    } else {
                        latitude = 3.58333
                        longitude = 98.66667
                    }
                    cont.resume(location)
                }
                .addOnFailureListener { e ->
                    cont.resume(null)
                }
        }
    }

    private fun loadWeatherData(context: Context): List<Weather>? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        val weatherDataString = sharedPreferences.getString("weather_data", null) ?: return null

        val weatherList = mutableListOf<Weather>()
        val jsonArray = JSONArray(weatherDataString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val time = jsonObject.getString("time")
            val temp = jsonObject.getString("temp")
            val wCode = jsonObject.getInt("weather_code")
            weatherList.add(Weather(time, temp, wCode))
        }
        return weatherList
    }

    fun fetchWeatherData(context: Context, onWeatherFetched: (List<Weather>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            getLatandLong()
            if (STATE.isOnline(context)) {
                val client = AsyncHttpClient()
                val url =
                    "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,weather_code&forecast_days=3"

                withContext(Dispatchers.Main) {
                    client.get(url, object : AsyncHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                            val result = String(responseBody)
                            try {
                                val resObj = JSONObject(result)
                                val hourly = resObj.getJSONObject("hourly")

                                val times = hourly.getJSONArray("time")
                                val temps = hourly.getJSONArray("temperature_2m")
                                val wCodes = hourly.getJSONArray("weather_code")

                                val weatherList = mutableListOf<Weather>()
                                for (i in 0 until times.length()) {
                                    val time = times.getString(i)
                                    val temp = temps.getString(i)
                                    val wCode = wCodes.getInt(i)
                                    weatherList.add(Weather(time, temp, wCode))
                                }

                                saveWeatherData(context, weatherList)
                                onWeatherFetched(weatherList)
                            } catch (e: Exception) {
                            }
                        }

                        override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                            Log.d("ERROR", "Failed to fetch data: $statusCode")
                        }
                    })
                }
            } else {
                val weatherList = loadWeatherData(context)
                if (weatherList != null) {
                    onWeatherFetched(weatherList)
                }
            }
        }
    }

    fun getTodaysWeather(){

    }
}