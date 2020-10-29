package com.jakode.covid19.data.api.model

import com.google.gson.annotations.SerializedName

data class GlobalNetworkEntity(
    val date: String,

    @SerializedName("last_update")
    val lastUpdate: String,

    val confirmed: Int,

    @SerializedName("confirmed_diff")
    val confirmedDiff: Int,

    val deaths: Int,

    @SerializedName("deaths_diff")
    val deathsDiff: Int,

    val recovered: Int,

    @SerializedName("recovered_diff")
    val recoveredDiff: Int,

    val active: Int,

    @SerializedName("active_diff")
    val activeDiff: Int,

    @SerializedName("fatality_rate")
    val fatalityRate: Float
)