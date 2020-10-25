package com.jakode.covid19.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
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
                controller.show(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    override fun onBackPressed() = true
}