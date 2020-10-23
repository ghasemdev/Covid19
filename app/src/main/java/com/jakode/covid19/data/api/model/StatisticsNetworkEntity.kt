package com.jakode.covid19.data.api.model

data class StatisticsNetworkEntity(
        val continent: String?,
        val country: String,
        val population: Int?,
        val cases: Cases,
        val deaths: Deaths,
        val tests: Tests,
        val day: String,
        val time: String
) {

    data class Cases(
            val new: String?,
            val active: Int?,
            val critical: Int?,
            val recovered: Int?,
            val total: Int?
    )

    data class Deaths(
            val new: String?,
            val total: Int?
    )

    data class Tests(
            val total: Int?
    )
}