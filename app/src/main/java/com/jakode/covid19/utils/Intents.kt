package com.jakode.covid19.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object Intents {
    private const val PACKAGE_NAME = "com.jakode.covid19"

    // Send comment
    fun sendComment(context: Context) {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("myket://comment?id=$PACKAGE_NAME")
        }.also { context.startActivity(it) }
    }

    // send Link
    fun sendText(context: Context) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "https://myket.ir/app/$PACKAGE_NAME")
        }.also { context.startActivity(Intent.createChooser(it, null)) }
    }
}