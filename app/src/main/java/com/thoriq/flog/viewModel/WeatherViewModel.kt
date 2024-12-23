package com.thoriq.flog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thoriq.flog.data.Lokasi
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

class WeatherViewModel(application: Application): ViewModel() {
    var weatherList: List<Weather> = emptyList()
    var isLoaded = MutableStateFlow(false)
    var lokasi: Lokasi

    private val mRepository: WeatherRepository = WeatherRepository(application)

    init {
        mRepository.fetchWeatherData(application){
            weatherList = it
            isLoaded.value = true
        }

        lokasi = mRepository.getLatLong()
    }
}