package com.jakode.covid19.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FragmentStatisticsBinding
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.ui.home.MainStateEvent
import com.jakode.covid19.utils.DataState
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
        refresh()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    @ExperimentalCoroutinesApi
    private fun refresh() {
        binding.apply {
//            refreshLayout.setOnRefreshListener {
//                displayError(false)
//                viewModel.setStateEvent(MainStateEvent.GetBlogEvents, true)
//                refreshLayout.isRefreshing = false
//            }
        }
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
            page.visibility = if (isDisplayed) View.GONE else View.VISIBLE
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.apply {
            loading.visibility = if (isDisplayed) View.VISIBLE else View.GONE
            page.visibility = if (isDisplayed) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = true
}