package com.jakode.covid19.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jakode.covid19.R
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.utils.OnBackPressedListener

class HomeFragment : Fragment(R.layout.fragment_home), OnBackPressedListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    override fun onBackPressed() = true
}