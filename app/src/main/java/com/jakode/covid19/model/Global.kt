package com.jakode.covid19.model

data class Global(
    var confirmed: Int,
    var deaths: Int,
    var recovered: Int,
    var active: Int,
    var lastUpdate: String,
)