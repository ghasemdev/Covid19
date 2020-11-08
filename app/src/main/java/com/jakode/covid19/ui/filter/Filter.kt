package com.jakode.covid19.ui.filter

import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.adapter.ViewType

data class Filter(
    var order: String,
    var continent: String,
    var sortBy: String
) {
    companion object {
        fun filter(statisticsList: List<ViewType<*>>, filter: Filter): List<ViewType<*>> {
            var list = statisticsList
            val order = filter.order.toBoolean()
            val continent = filter.continent
            val sortBy = filter.sortBy

            if (continent != "All")
                list = list.filter { (it.data() as Statistics).continent == continent }

            return when {
                order && sortBy == "Total Case" -> list.sortedBy { (it.data() as Statistics).cases.total }
                order && sortBy == "Deaths" -> list.sortedBy { (it.data() as Statistics).deaths.total }
                order && sortBy == "Country" -> list.sortedByDescending { (it.data() as Statistics).country }
                order && sortBy == "Recovered" -> list.sortedBy { (it.data() as Statistics).cases.recovered }

                !order && sortBy == "Total Case" -> list.sortedByDescending { (it.data() as Statistics).cases.total }
                !order && sortBy == "Deaths" -> list.sortedByDescending { (it.data() as Statistics).deaths.total }
                !order && sortBy == "Country" -> list.sortedBy { (it.data() as Statistics).country }
                !order && sortBy == "Recovered" -> list.sortedByDescending { (it.data() as Statistics).cases.recovered }
                else -> list
            }
        }
    }
}