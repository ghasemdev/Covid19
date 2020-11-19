package com.jakode.covid19.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object Intents {
    // Send email
    fun composeEmail(context: Context, to: Array<String>, subject: String, text: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, to)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }.also { context.startActivity(it) }
    }

    // send Link
    fun sendText(context: Context, text: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }.also { context.startActivity(Intent.createChooser(it, null)) }
    }
}