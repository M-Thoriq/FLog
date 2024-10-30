package com.thoriq.flog.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.thoriq.flog.data.Fish
import com.thoriq.flog.repository.FishRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FishViewModel(application: Application): ViewModel() {
    private val mRepository: FishRepository = FishRepository(application)

    fun getAllFish() = mRepository.readAllData()
    fun getFishById(id: Int) : Flow<Fish> = mRepository.getFishById(id)

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun insertFish(fish: Fish) {
        fish.createdAt = getCurrentDate()
        mRepository.insertFish(fish)
    }

    fun updateFish(fish: Fish, oldFishId: Int){
        Log.d("UPDATE", "VIEWMODEL: $fish")
        fish.createdAt = getCurrentDate()
        fish.id = oldFishId
        mRepository.updateFish(fish)
    }

    fun deleteFish(fish: Fish){
        fish.createdAt = getCurrentDate()
        mRepository.deleteFish(fish)
    }
}