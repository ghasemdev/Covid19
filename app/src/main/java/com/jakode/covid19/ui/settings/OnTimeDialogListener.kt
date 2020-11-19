package com.jakode.covid19.ui.settings

import android.os.Parcelable

interface OnTimeDialogListener : Parcelable {
    fun onChanged(hour: Int, minute: Int, handlers: TimeDialog.MyHandlers)
}