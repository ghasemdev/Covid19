package com.jakode.covid19.ui.search

import com.jakode.covid19.R
import com.jakode.covid19.model.SearchHistory
import com.jakode.covid19.ui.adapter.ViewType

data class SearchHistoryViewType(private val model: SearchHistory) : ViewType<SearchHistory> {
    override fun layoutId(): Int = R.layout.search_history_item
    override fun data(): SearchHistory = model
}