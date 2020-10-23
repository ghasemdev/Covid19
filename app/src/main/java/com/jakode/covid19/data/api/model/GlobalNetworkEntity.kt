package com.jakode.covid19.data.api.model

data class GlobalNetworkEntity(
        val recovered: Int,
        val deaths: Int,
        val confirmed: Int,
        val lastChecked: String,
        val lastReported: String,
        val location: String
)