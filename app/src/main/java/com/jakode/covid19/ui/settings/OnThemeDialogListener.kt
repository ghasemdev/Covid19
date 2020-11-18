package com.jakode.covid19.ui.settings

import android.os.Parcelable

interface OnThemeDialogListener : Parcelable {
    fun onChanged(theme: Boolean?, handlers: ThemeDialog.MyHandlers)
}