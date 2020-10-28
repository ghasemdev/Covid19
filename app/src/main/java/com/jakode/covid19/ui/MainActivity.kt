package com.jakode.covid19.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakode.covid19.R
import com.jakode.covid19.utils.OnBackPressedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var onBackPressedListener: OnBackPressedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // This function is called by fragments
    fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener
    }

    override fun onBackPressed() {
        if (onBackPressedListener.onBackPressed()) super.onBackPressed()
    }
}