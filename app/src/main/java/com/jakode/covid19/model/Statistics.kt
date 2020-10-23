package com.jakode.covid19.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Statistics(
        var continent: String?,
        var country: String,
        var population: Int?,
        var cases: @RawValue Cases,
        var deaths: @RawValue Deaths,
        var tests: @RawValue Tests,
        var day: String,
        var time: String
) : Parcelable {

    data class Cases(
            var new: String?,
            var active: Int?,
            var critical: Int?,
            var recovered: Int?,
            var total: Int?
    )

    data class Deaths(
            var new: String?,
            var total: Int?
    )

    data class Tests(
            var total: Int?
    )
}