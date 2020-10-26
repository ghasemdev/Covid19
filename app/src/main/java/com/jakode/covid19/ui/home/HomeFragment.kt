package com.jakode.covid19.ui.home

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.jakode.covid19.R
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.utils.OnBackPressedListener

class HomeFragment : Fragment(R.layout.fragment_home), OnBackPressedListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showStatusBar() // Show ui system

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun showStatusBar() {
        requireActivity().apply {
            if (Build.VERSION.SDK_INT >= 30) {
                window.setDecorFitsSystemWindows(true)
                val controller: WindowInsetsController = requireActivity().window.insetsController!!
                controller.show(WindowInsets.Type.navigationBars())
            }
        }
    }

    override fun onBackPressed() = true
}