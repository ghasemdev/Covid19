package com.jakode.covid19.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "global")
data class GlobalCacheEntity(
        var recovered: Int,
        var deaths: Int,
        var confirmed: Int,
        var lastChecked: String,
        var lastReported: String,

        @PrimaryKey(autoGenerate = false)
        val location: String
)