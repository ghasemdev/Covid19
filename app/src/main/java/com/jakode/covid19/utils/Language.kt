package com.jakode.covid19.utils

import java.util.*

object LocaleCodes {
    const val ENGLISH = "en"
    const val PERSIAN = "fa"
}

enum class Language(val locale: Locale) {
    English(Locale(LocaleCodes.ENGLISH)),
    Persian(Locale(LocaleCodes.PERSIAN));

    companion object {
        val DEFAULT = English

        fun fromLocale(locale: Locale): Language =
            values().firstOrNull { it.locale.language == locale.language } ?: DEFAULT

        fun getName(language: Language): String {
            return when (language) {
                Persian -> "فارسی"
                English -> "English"
            }
        }
    }
}