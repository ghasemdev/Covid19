package com.jakode.covid19.ui.activities

import android.os.Bundle
import com.jakode.covid19.R
import com.jakode.covid19.data.sharedpreferences.SharedPreferencesHelper
import com.jakode.covid19.utils.OnBackPressedListener
import com.jakode.covid19.utils.changeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var onBackPressedListener: OnBackPressedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = SharedPreferencesHelper(this).theme
        if (theme != null) { changeTheme(theme) }
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