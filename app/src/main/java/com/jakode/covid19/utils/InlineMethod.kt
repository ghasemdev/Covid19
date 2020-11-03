package com.jakode.covid19.utils

import com.jakode.covid19.model.Statistics

typealias Predicate<T> = (T) -> Boolean

inline fun statisticsFilter(
    statistics: List<Statistics>,
    predicate: Predicate<Statistics>
): List<Statistics> {
    return statistics.filter(predicate)
}

inline fun statisticsDesSort(
    statistics: List<Statistics>,
    predicate: Predicate<Statistics>
): List<Statistics> {
    return statistics.sortedWith(
        compareBy(
            { it.cases.total },
            { it.deaths.total },
            { it.cases.recovered })
    ).filter(predicate).reversed()
}