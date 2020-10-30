package com.jakode.covid19.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistics")
data class StatisticsCacheEntity(
        val continent: String?,

        @PrimaryKey(autoGenerate = false)
        val country: String,

        val population: Int?,

        @Embedded
        val cases: Cases?,

        @Embedded
        val deaths: Deaths?,

        @Embedded
        val tests: Tests?,

        val day: String,

        val time: String
) {
    data class Cases(
            @ColumnInfo(name = "new_cases")
            val newCases: String?,

            @ColumnInfo(name = "active_cases")
            val activeCases: Int?,

            @ColumnInfo(name = "critical_cases")
            val criticalCases: Int?,

            @ColumnInfo(name = "recovered_cases")
            val recoveredCases: Int?,

            @ColumnInfo(name = "total_cases")
            val totalCases: Int?
    )

    data class Deaths(
            @ColumnInfo(name = "new_deaths")
            val newDeaths: String?,

            @ColumnInfo(name = "total_deaths")
            val totalDeaths: Int?
    )

    data class Tests(
            @ColumnInfo(name = "total_tests")
            val totalTests: Int?
    )
}