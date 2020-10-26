package com.jakode.covid19.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jakode.covid19.R
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.utils.OnBackPressedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash), OnBackPressedListener {
    private val viewModel by viewModels<SplashViewModel>()
    private var state by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate() // Navigate to other page
        observe() // Observe state

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            this.state = state
        })
    }

    private fun navigate() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            if (state)
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            else
                findNavController().navigate(R.id.action_splashFragment_to_introFragment)
        }
    }

    override fun onBackPressed() = false
}