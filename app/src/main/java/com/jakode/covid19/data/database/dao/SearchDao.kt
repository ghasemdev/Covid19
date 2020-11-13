package com.jakode.covid19.data.database.dao

import androidx.room.*
import com.jakode.covid19.data.database.model.SearchHistoryEntity

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg historyEntity: SearchHistoryEntity)

    @Update
    suspend fun update(vararg historyEntity: SearchHistoryEntity)

    @Delete
    suspend fun delete(vararg historyEntity: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()

    @Query("SELECT * From search_history")
    suspend fun getAll(): List<SearchHistoryEntity>

    @Query("SELECT * From search_history WHERE `query` = :query")
    suspend fun getByQuery(query: String): SearchHistoryEntity?
}