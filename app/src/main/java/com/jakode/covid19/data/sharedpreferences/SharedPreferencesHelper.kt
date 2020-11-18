package com.jakode.covid19.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jakode.covid19.utils.Language
import com.jakode.covid19.utils.LocalHelper.Companion.getDefaultLanguage

class SharedPreferencesHelper {
    companion object {
        private var pref: SharedPreferences? = null

        private const val PREFERENCE_NAME = "SETTINGS"
        private const val PREF_LANG = "LANGUAGE"
        private const val PREF_UI = "THEME"

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it
                }
            }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return SharedPreferencesHelper()
        }
    }

    var language: Language
        set(value) {
            pref?.edit(commit = true) {
                putString(PREF_LANG, value.toString())
            }
        }
        get() {
            val storedValue = pref?.getString(PREF_LANG, "")!!
            val storedLanguage = try {
                Language.valueOf(storedValue)
            } catch (ex: Exception) {
                null
            }
            return storedLanguage ?: getDefaultLanguage()
        }

    var theme: Boolean?
        set(value) {
            pref?.edit(commit = true) {
                putInt(
                    PREF_UI, if (value != null) {
                        if (value) 1 else 2
                    } else 0
                )
            }
        }
        get() {
            return when (pref?.getInt(PREF_UI, 0)) {
                1 -> true
                2 -> false
                else -> null
            }
        }
}