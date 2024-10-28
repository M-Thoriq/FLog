package com.thoriq.flog.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.thoriq.flog.data.Fish
import com.thoriq.flog.repository.FishRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FishViewModel(application: Application): ViewModel() {
    private val mRepository: FishRepository = FishRepository(application)

    fun getAllFish() = mRepository.readAllData()

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun insertFish(fish: Fish) {
        fish.createdAt = getCurrentDate()
        mRepository.insertFish(fish)
    }

    fun updateFish(fish: Fish){
        mRepository.updateFish(fish)
    }

    fun deleteFish(fish: Fish){
        mRepository.deleteFish(fish)
    }
}