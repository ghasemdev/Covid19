package com.jakode.covid19.model

import java.util.*

data class SearchHistory(
    var id: Long = 0,
    var query: String,
    var date: Date?
)