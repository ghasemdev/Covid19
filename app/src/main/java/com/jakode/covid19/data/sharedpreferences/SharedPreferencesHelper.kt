package com.jakode.covid19.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.jakode.covid19.utils.Language
import com.jakode.covid19.utils.LocalHelper.Companion.getDefaultLanguage

class SharedPreferencesHelper {
    companion object {
        private var pref: SharedPreferences? = null

        private const val PREFERENCE_NAME = "SETTINGS"
        private const val PREF_LANG = "LANGUAGE"

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
            pref!!.edit().putString(PREF_LANG, value.toString()).apply()
        }
        get() {
            val storedValue = pref!!.getString(PREF_LANG, "")!!
            val storedLanguage = try {
                Language.valueOf(storedValue)
            } catch (ex: Exception) {
                null
            }
            return storedLanguage ?: getDefaultLanguage()
        }
}