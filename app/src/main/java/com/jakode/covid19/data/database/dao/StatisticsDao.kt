package com.jakode.covid19.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jakode.covid19.data.database.model.StatisticsCacheEntity

@Dao
interface StatisticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: StatisticsCacheEntity)

    @Query("SELECT * FROM statistics WHERE continent != country")
    suspend fun getAll(): List<StatisticsCacheEntity>
}