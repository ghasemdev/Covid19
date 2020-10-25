package com.jakode.covid19.ui.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : Fragment(R.layout.fragment_intro) {
    private val viewModel by viewModels<IntroViewModel>()

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    // Set pages fragment as list
    private val pages = listOf(
        WashYourHands(),
        WearMask(),
        UseNoseRag()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIntroBinding.bind(view)

        binding.apply {
            // Set adapter
            viewPager.adapter = IntroAdapter(
                pages,
                requireActivity().supportFragmentManager,
                lifecycle
            )

            // Connect tabLayout and viewPager2
            TabLayoutMediator(tabIndicator, viewPager) { _, _ -> }.attach()

            // Manage animation button with tabLayout
            tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // First page skip button is available
                    buttonSkip.visibility =
                        if (tab!!.position == 0) View.VISIBLE else View.INVISIBLE
                    // Last page load animation and when back to previous page unload animation
                    if (tab.position == pages.size - 1) loadLastScreen() else unLoadLastScreen()
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
            })
        }

        // onClickListener
        onClickListenerManager()
    }

    private fun onClickListenerManager() {
        binding.apply {
            // Click skip button going to last page
            buttonSkip.setOnClickListener {
                viewPager.currentItem = pages.size - 1
            }

            // Click next button going to next page
            buttonNext.setOnClickListener {
                var position = viewPager.currentItem
                if (position < pages.size) {
                    position++
                    viewPager.currentItem = position
                }
                when (position) {
                    // Last page load animation
                    pages.size - 1 -> loadLastScreen()
                    // Click start button going to main page
                    pages.size -> {
                        viewModel.saveState(true)
                        launchMainFragment()
                    }
                }
            }
        }
    }

    private fun loadLastScreen() {
        binding.apply {
            buttonNext.text = getString(R.string.start_now)
            // setup animation
            buttonNext.translationY = -100f
            buttonNext.animate().translationY(0f).duration = 700
        }
    }

    private fun unLoadLastScreen() {
        binding.apply {
            buttonNext.text = getString(R.string.next_step)
        }
    }

    private fun launchMainFragment() {
        findNavController().navigate(R.id.action_introFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}