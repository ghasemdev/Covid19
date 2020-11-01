package com.jakode.covid19.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FragmentStatisticsBinding
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.ui.home.MainStateEvent
import com.jakode.covid19.utils.AppBarStateChangeListener
import com.jakode.covid19.utils.DataState
import com.jakode.covid19.utils.popup.DetailsPopup
import com.jakode.covid19.utils.OnBackPressedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics), OnBackPressedListener {
    private val viewModel: StatisticViewModel by viewModels()
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private var statisticsList: List<Statistics> = listOf()
    private var statisticsAdapter: StatisticsAdapter? = null

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)
        observe()
        parallax()
        toolbar()
        header()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun header() {
        binding.toolbarHeader.apply {
            backInHeader.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun toolbar() {
        // Set toolbar and menu
        binding.apply {
            (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            toolbarMore.setOnClickListener { }
            toolbarRefresh.setOnClickListener {
                displayError(false)
                viewModel.setStateEvent(MainStateEvent.GetBlogEvents, true)
            }
            toolbarSearch.setOnClickListener { }

            toolbarMore.setOnLongClickListener {
                popup(R.id.toolbar_more, 0, getString(R.string.more))
            }
            toolbarRefresh.setOnLongClickListener {
                popup(R.id.toolbar_refresh, -50, getString(R.string.refresh))
            }
            toolbarSearch.setOnLongClickListener {
                popup(R.id.toolbar_search, -50, getString(R.string.search))
            }
        }
    }

    private fun popup(resource: Int, x: Int, massage: String): Boolean {
        DetailsPopup().Builder {
            anchor = requireView().findViewById(resource)
            layoutResource = R.layout.details_popup_layout
            coordinates.x = x
            elevation = 5F
            popupTexts = listOf(massage)
        }.build()
        return true
    }

    private fun parallax() {
        binding.appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                binding.toolbar.visibility = when (state) {
                    State.EXPANDED -> View.GONE
                    State.COLLAPSED -> View.VISIBLE
                    else -> View.GONE
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents, false)
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<List<Statistics>> -> {
                    displayProgressBar(false)
                    setStatistic(dataState.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(true, dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun setStatistic(statistics: List<Statistics>) {
        statisticsList = statistics
        countryRecycler()
    }

    private fun countryRecycler() {
        statisticsAdapter = StatisticsAdapter(statisticsList)
        binding.statisticsList.apply {
            adapter = statisticsAdapter!!
            setHasFixedSize(true)
        }
    }

    private fun displayError(isDisplayed: Boolean, massage: String? = "") {
        if (massage != null)
            if (massage.isNotEmpty())
                Log.e("ERROR", "displayError: $massage")
        binding.apply {
            connectionError.visibility = if (isDisplayed) View.VISIBLE else View.GONE
            connectionErrorText.visibility = if (isDisplayed) View.VISIBLE else View.GONE
            nestedScrollView.visibility = if (isDisplayed) View.GONE else View.VISIBLE
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.apply {
            loading.visibility = if (isDisplayed) View.VISIBLE else View.GONE
            nestedScrollView.visibility = if (isDisplayed) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = true
}