package com.jakode.covid19.utils

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.util.*

fun formatNumber(value: Float): String {
    val mFormat = DecimalFormat("###,###,###,###")
    return mFormat.format(value.toDouble())
}

fun formatFloat(value: Float): String {
    val mFormat = DecimalFormat("#.##")
    return mFormat.format(value.toDouble())
}

fun formatShortcut(value: Int): String {
    return when {
        value >= 1_000_000 -> "${formatFloat(value / 1_000_000F)} ${if (isPersian()) "میلیون" else "M"}"
        value >= 1000 -> "${formatFloat(value / 1000F)} ${if (isPersian()) "هزار" else "K"}"
        else -> value.toString()
    }
}

fun isDarkTheme(activity: Activity): Boolean {
    return activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun getColor(colorHex: String): Int {
    var color = colorHex
    if (color[0] != '#') color = "#$color"

    return Color.parseColor(color)
}

fun toPersian(text: String?): String? {
    return if (isPersian()) {
        val persianText = StringBuilder()
        fun iterate(letter: Char) {
            persianText.apply {
                when (letter) {
                    '0' -> append("۰")
                    '1' -> append("۱")
                    '2' -> append("۲")
                    '3' -> append("۳")
                    '4' -> append("۴")
                    '5' -> append("۵")
                    '6' -> append("۶")
                    '7' -> append("۷")
                    '8' -> append("۸")
                    '9' -> append("۹")
                    else -> append(letter)
                }
            }
        }
        text?.let {
            val arrayText = it.split(" ")
            if (arrayText.size > 1) {
                for (index in arrayText.size - 1 downTo 0) {
                    for (letter in arrayText[index])
                        iterate(letter)
                    persianText.append(" ")
                }
            } else {
                for (letter in text) iterate(letter)
            }
        }
        persianText.toString()
    } else text
}

fun isPersian() = Locale.getDefault().language == "fa"