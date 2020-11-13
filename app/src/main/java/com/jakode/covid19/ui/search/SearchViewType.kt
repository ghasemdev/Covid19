package com.jakode.covid19.ui.search

import com.jakode.covid19.R
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.adapter.ViewType

data class SearchViewType(private val model: Statistics) : ViewType<Statistics> {
    override fun layoutId(): Int = R.layout.search_item
    override fun data(): Statistics = model
}