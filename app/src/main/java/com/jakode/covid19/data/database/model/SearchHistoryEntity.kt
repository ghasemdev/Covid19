package com.jakode.covid19.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val query: String,
    val date: Date?
) {
    constructor(query: String, date: Date?) : this(0, query, date)
}