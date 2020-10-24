package com.jakode.covid19.ui.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jakode.covid19.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val viewModel by viewModels<SplashViewModel>()
    private var state by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullScreen() // Full screen size
        observe() // Observe state
        navigate() // Navigate to other page
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            this.state = state
        })
    }

    private fun navigate() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            if (state)
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            else
                findNavController().navigate(R.id.action_splashFragment_to_introFragment)
        }
    }

    private fun fullScreen() {
        requireActivity().apply {
            if (Build.VERSION.SDK_INT >= 30) {
                window.setDecorFitsSystemWindows(false)
                val controller: WindowInsetsController = window.insetsController!!
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
        }
    }
}