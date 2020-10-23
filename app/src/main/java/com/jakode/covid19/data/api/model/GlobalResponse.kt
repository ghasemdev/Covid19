package com.jakode.covid19.data.api.model

import com.google.gson.annotations.SerializedName

data class GlobalResponse(
        @SerializedName("data")
        var global: GlobalNetworkEntity
)