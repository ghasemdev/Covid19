package com.jakode.covid19.model

data class Global(
        var recovered: Int,
        var deaths: Int,
        var confirmed: Int,
        var lastChecked: String,
        var lastReported: String,
)