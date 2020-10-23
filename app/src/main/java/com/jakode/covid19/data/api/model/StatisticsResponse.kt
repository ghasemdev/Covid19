package com.jakode.covid19.data.api.model

import com.google.gson.annotations.SerializedName

data class StatisticsResponse(
        @SerializedName("response")
        var statistics: List<StatisticsNetworkEntity>
)