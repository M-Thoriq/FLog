package com.thoriq.flog.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.thoriq.flog.config.FishDatabase
import com.thoriq.flog.data.Fish
import com.thoriq.flog.data.dao.FishDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FishRepository(application: Application) {
    private val mFishDao : FishDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FishDatabase.getDatabase(application)
        mFishDao = db.fishDao()
    }

    fun readAllData() : Flow<List<Fish>> = mFishDao.getAllFish()

    fun insertFish(fish: Fish) {
        CoroutineScope(Dispatchers.IO).launch {
            mFishDao.insertFish(fish)
        }
    }

    fun updateFish(fish: Fish) {
        CoroutineScope(Dispatchers.IO).launch {
            mFishDao.updateFish(fish)
        }
    }

    fun deleteFish(fish: Fish) {
        CoroutineScope(Dispatchers.IO).launch {
            mFishDao.deleteFish(fish)
        }
    }
}