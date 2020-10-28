package com.jakode.covid19.utils

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import java.text.DecimalFormat

fun formatNumber(value: Float): String {
    val mFormat = DecimalFormat("###,###,###,###")
    return mFormat.format(value.toDouble())
}

fun isDarkTheme(activity: Activity): Boolean {
    return activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun getColor(colorHex: String): Int {
    var color = colorHex
    if (color[0] != '#') color = "#$color"

    return Color.parseColor(color)
}