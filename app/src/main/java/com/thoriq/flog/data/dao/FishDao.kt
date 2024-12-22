package com.thoriq.flog.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.thoriq.flog.data.Fish
import kotlinx.coroutines.flow.Flow

@Dao
interface FishDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFish(fish: Fish)

    @Update
    suspend fun updateFish(fish: Fish)

    @Delete
    suspend fun deleteFish(fish: Fish)

    @Query("SELECT * FROM fishes")
    fun getAllFish(): Flow<List<Fish>>

    @Query("SELECT * FROM fishes WHERE id = :id")
    fun getFishById(id: Int): Flow<Fish>

    @Query("SELECT COUNT(*) FROM fishes")
    fun getCount(): Flow<Int>
}