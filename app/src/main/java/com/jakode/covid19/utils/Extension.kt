package com.jakode.covid19.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
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
        if (text != null) {
            for (letter in text) {
                if (letter == '+') break
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
                        'T' -> append(" ")
                        else -> append(letter)
                    }
                }
            }
        }
        persianText.toString()
    } else {
        text?.let { it.replace("T", " ").substring(0, it.lastIndex - 5) }
    }
}

fun isPersian() = Locale.getDefault().language == "fa"

/**
 * Get resource from name
 * @param context
 * @param country
 * @return Int resource
 */
fun getResources(context: Context, country: String): Int {
    val resources: Resources = context.resources
    return resources.getIdentifier(
        "ic_${country.toLowerCase(Locale.ROOT)}", "drawable",
        context.packageName
    )
}