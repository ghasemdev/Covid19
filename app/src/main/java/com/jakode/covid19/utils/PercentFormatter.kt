package com.jakode.covid19.utils

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

/**
 * This IValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommended for PieChart)
 *
 * @author Jakode
 */
class PercentFormatter : ValueFormatter() {
    private var mFormat: DecimalFormat = DecimalFormat("###,###,##0.0")

    override fun getFormattedValue(value: Float) = "${mFormat.format(value.toDouble())} %"

    override fun getPieLabel(value: Float, pieEntry: PieEntry) = getFormattedValue(value)
}