package com.jakode.covid19.ui.statistics

import com.jakode.covid19.R
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.adapter.ViewType

data class StatisticsViewType(private val model: Statistics) : ViewType<Statistics> {
    override fun layoutId(): Int = R.layout.statistic_item
    override fun data(): Statistics = model
}