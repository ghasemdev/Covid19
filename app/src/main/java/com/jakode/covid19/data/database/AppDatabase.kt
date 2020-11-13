package com.jakode.covid19.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.SearchDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import com.jakode.covid19.data.database.model.GlobalCacheEntity
import com.jakode.covid19.data.database.model.SearchHistoryEntity
import com.jakode.covid19.data.database.model.StatisticsCacheEntity
import com.jakode.covid19.utils.Converters

@Database(
    entities = [StatisticsCacheEntity::class, GlobalCacheEntity::class, SearchHistoryEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statisticsDao(): StatisticsDao
    abstract fun globalDao(): GlobalDao
    abstract fun searchDao(): SearchDao

    companion object {
        const val DATABASE_NAME = "covid_db"
    }
}