package com.jakode.covid19.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FragmentHomeBinding
import com.jakode.covid19.model.Global
import com.jakode.covid19.model.StatisticsAndGlobal
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.ui.adapter.ViewType
import com.jakode.covid19.ui.adapter.ViewTypeAdapter
import com.jakode.covid19.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnBackPressedListener,
    OnChartValueSelectedListener {
    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var topCountryAdapter: ViewTypeAdapter<ViewType<*>>? = null

    private lateinit var globalInfo: List<PieEntry>
    private val chartColors = listOf(
        getColor("55E13A"),
        getColor("FFC259"),
        getColor("FF5959")
    )

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        topCountryRecycler()
        observe()
        refresh()
        globalExpand()
        clickListener()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun clickListener() {
        binding.apply {
            viewAll.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_statisticsFragment)
            }
        }
    }

    private fun globalExpand() {
        binding.apply {
            expand.setOnClickListener { // Open card view
                if (expandLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(information, AutoTransition())
                    expandLayout.visibility = View.VISIBLE
                    expand.animate().rotationBy(180F).duration = 400
                } else { // Close card view
                    TransitionManager.beginDelayedTransition(information, AutoTransition())
                    expandLayout.visibility = View.GONE
                    expand.animate().rotationBy(180F).duration = 400
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun refresh() {
        binding.apply {
            refreshLayout.setOnRefreshListener {
                displayError(false)
                viewModel.setStateEvent(MainStateEvent.GetBlogEvents, true)
                refreshLayout.isRefreshing = false
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents, false)
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<StatisticsAndGlobal> -> {
                    displayProgressBar(false)
                    setStatistics(dataState.data.statistics)
                    setGlobalInfo(dataState.data.global)
                    binding.global = dataState.data.global
                    pieChart()
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

    private fun setStatistics(statistics: List<Statistics>) {
        val topCountry = ArrayList<ViewType<*>>(statistics.map { TopCountryViewType(it) })
        topCountryAdapter!!.setList(topCountry)
    }

    private fun topCountryRecycler() {
        topCountryAdapter = ViewTypeAdapter()
        binding.topCountry.apply {
            adapter = topCountryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setGlobalInfo(global: Global) {
        globalInfo = arrayListOf(
            PieEntry(global.recovered.toFloat(), "recovered"),
            PieEntry(global.confirmed.toFloat(), "confirmed"),
            PieEntry(global.deaths.toFloat(), "deaths")
        )
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

    private fun pieChart() {
        val pieDataSet = PieDataSet(globalInfo, "").apply {
            colors = chartColors                                    // Color of slice
            valueFormatter = PercentFormatter()                     // Formatting to %
            valueTextSize = 16F                                     // Size of text value
            valueTextColor = Color.WHITE                            // Color of values
        }

        val pieData = PieData(pieDataSet)
        binding.pieChart.apply {
            data = pieData                                          // Set data

            setDrawEntryLabels(false)                               // Hide label
            description.isEnabled = false                           // Hide description of chart
            legend.apply { isEnabled = false }                      // Hide color description
            setDrawCenterText(true)                                 // Center text
            setUsePercentValues(true)                               // Use %

            holeRadius = 58f                                        // Size of hole
            setHoleColor(                                           // Color of hole
                if (isDarkTheme(requireActivity())) getColor("121212")
                else getColor("FFFFFF")
            )

            transparentCircleRadius = 61f                           // Size of hole shadow
            setTransparentCircleColor(                              // Color of hole shadow
                if (isDarkTheme(requireActivity())) getColor("121212")
                else getColor("FFFFFF")
            )
            setTransparentCircleAlpha(110)                          // Alpha of hole shadow

            setCenterTextSize(22F)                                  // Size of center text
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)            // Center text bold
            setCenterTextColor(                                     // Color center text
                if (isDarkTheme(requireActivity())) getColor("FDFDFD")
                else getColor("232323")
            )

            setExtraOffsets(0F, 5F, 0F, 5F)  // Size of pie

            dragDecelerationFrictionCoef = 0.95f                    // Speed of animate
            animateXY(1400, 1400)         // Chart animation

            setOnChartValueSelectedListener(this@HomeFragment)      // Listener for slice
        }
    }

    override fun onValueSelected(entry: Entry, highlight: Highlight) {
        binding.pieChart.apply {
            centerText = when (highlight.x) { // Index
                0F -> formatNumber(globalInfo[0].value) // recovered
                1F -> formatNumber(globalInfo[1].value) // confirmed
                2F -> formatNumber(globalInfo[2].value) // deaths
                else -> "Nothing happen!!!"
            }
        }
    }

    override fun onNothingSelected() {
        binding.pieChart.centerText = ""
    }

    override fun onDestroyView() {
        topCountryAdapter = null
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = true
}