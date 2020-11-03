package com.jakode.covid19.ui.home

import com.jakode.covid19.R
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.adapter.ViewType

data class TopCountryViewType(private val model: Statistics) : ViewType<Statistics> {
    override fun layoutId(): Int = R.layout.top_country_item
    override fun data(): Statistics = model
}