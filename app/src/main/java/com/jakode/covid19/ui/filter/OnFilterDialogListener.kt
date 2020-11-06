package com.jakode.covid19.ui.filter

import android.os.Parcelable

interface OnFilterDialogListener : Parcelable {
    fun onApplyClicked(
        handlers: FilterDialog.MyHandlers,
        order: Boolean,
        continent: String,
        sortBy: String
    )
}