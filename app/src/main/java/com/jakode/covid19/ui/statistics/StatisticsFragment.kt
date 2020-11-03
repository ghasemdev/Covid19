package com.jakode.covid19.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FragmentStatisticsBinding
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.ui.adapter.ViewType
import com.jakode.covid19.ui.adapter.ViewTypeAdapter
import com.jakode.covid19.ui.home.MainStateEvent
import com.jakode.covid19.utils.DataState
import com.jakode.covid19.utils.OnBackPressedListener
import com.jakode.covid19.ui.popup.DetailsPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics), OnBackPressedListener {
    private val viewModel: StatisticViewModel by viewModels()
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private var statisticsAdapter: ViewTypeAdapter<ViewType<*>>? = null

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)
        observe()
        toolbar()
        header()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun header() {
        binding.header.apply {

        }
    }

    private fun toolbar() {
        // Set toolbar and menu
        binding.apply {
            toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }

            toolbarMore.setOnClickListener { }

            toolbarSearch.setOnClickListener { }

            toolbarMore.setOnLongClickListener {
                popup(R.id.toolbar_more, getString(R.string.more))
            }

            toolbarSearch.setOnLongClickListener {
                popup(R.id.toolbar_search, getString(R.string.search))
            }
        }
    }

    private fun popup(resource: Int, massage: String): Boolean {
        DetailsPopup().Builder {
            anchor = requireView().findViewById(resource)
            layoutResource = R.layout.details_popup_layout
            coordinates.x = -50
            elevation = 5F
            popupTexts = listOf(massage)
        }.build()
        return true
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
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun setStatistic(statistics: List<Statistics>) {
        val statisticsList = ArrayList<ViewType<*>>(statistics.map { StatisticsViewType(it) })
        countryRecycler(statisticsList)
    }

    private fun countryRecycler(statisticsList: ArrayList<ViewType<*>>) {
        statisticsAdapter = ViewTypeAdapter(statisticsList)
        binding.statisticsList.apply {
            adapter = statisticsAdapter!!
            setHasFixedSize(true)
        }
    }

    private fun displayError(massage: String? = "") {
        if (massage != null)
            if (massage.isNotEmpty())
                Log.e("ERROR", "displayError: $massage")
        binding.apply {
            connectionError.visibility = View.VISIBLE
            connectionErrorText.visibility = View.VISIBLE
            statisticsList.visibility = View.GONE
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.apply {
            loading.visibility = if (isDisplayed) View.VISIBLE else View.GONE
            statisticsList.visibility = if (isDisplayed) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        statisticsAdapter = null
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = true
}