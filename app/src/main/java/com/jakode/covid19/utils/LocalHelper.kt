package com.jakode.covid19.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

class LocalHelper(base: Context) : ContextWrapper(base) {
    companion object {
        fun getDefaultLanguage(): Language {
            val locale = Locale.getDefault()
            return Language.fromLocale(locale)
        }

        fun updateLocale(context: Context, localeToSwitchTo: Locale): ContextWrapper {
            var mContext = context
            val resources: Resources = mContext.resources
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                configuration.locale = localeToSwitchTo
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                mContext = mContext.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return LocalHelper(mContext)
        }
    }
}