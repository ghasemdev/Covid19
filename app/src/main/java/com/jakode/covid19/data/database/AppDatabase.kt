package com.jakode.covid19.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import com.jakode.covid19.data.database.model.GlobalCacheEntity
import com.jakode.covid19.data.database.model.StatisticsCacheEntity

@Database(entities = [StatisticsCacheEntity::class, GlobalCacheEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statisticsDao(): StatisticsDao
    abstract fun globalDao(): GlobalDao

    companion object {
        const val DATABASE_NAME = "covid_db"
    }
}