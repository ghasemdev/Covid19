package com.jakode.covid19.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jakode.covid19.data.database.model.GlobalCacheEntity

@Dao
interface GlobalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GlobalCacheEntity)

    @Query("SELECT * FROM global")
    suspend fun get(): GlobalCacheEntity
}