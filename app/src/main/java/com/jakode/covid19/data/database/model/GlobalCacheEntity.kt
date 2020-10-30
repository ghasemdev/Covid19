package com.jakode.covid19.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "global")
data class GlobalCacheEntity(
    var confirmed: Int,
    var deaths: Int,
    var recovered: Int,
    var active: Int,

    @ColumnInfo(name = "last_update")
    var lastUpdate: String,

    @PrimaryKey(autoGenerate = false)
    val location: String
)