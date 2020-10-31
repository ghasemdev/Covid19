package com.jakode.covid19.model

data class StatisticsAndGlobal(
    var global: Global,
    var statistics: List<Statistics>
)