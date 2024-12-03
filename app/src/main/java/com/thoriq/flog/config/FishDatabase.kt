package com.thoriq.flog.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thoriq.flog.data.Fish
import com.thoriq.flog.data.dao.FishDao

@Database(entities = [Fish::class], version = 2)
abstract class FishDatabase : RoomDatabase() {
    abstract fun fishDao(): FishDao

    companion object{
        @Volatile
        private var INSTANCE: FishDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FishDatabase::class.java,
                    "fish_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }


}