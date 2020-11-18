package com.jakode.covid19.ui.settings

import android.os.Parcelable
import com.jakode.covid19.utils.Language

interface OnLanguageDialogListener : Parcelable {
    fun onChanged(language: Language, handlers: LanguageDialog.MyHandlers)
}