package com.jakode.covid19.ui.statistics

import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FragmentStatisticsBinding
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.MainActivity
import com.jakode.covid19.ui.adapter.ViewType
import com.jakode.covid19.ui.adapter.ViewTypeAdapter
import com.jakode.covid19.ui.dialogs.PopupMenu
import com.jakode.covid19.ui.filter.FilterDialog
import com.jakode.covid19.ui.filter.OnFilterDialogListener
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

    private var statisticsAdapter: ViewTypeAdapter<ViewType<*>>? = null
    private lateinit var statisticsList: List<ViewType<*>>

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)
        countryRecycler()
        observe()
        toolbar()
        header()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun header() {
        binding.header.apply {
            cardFilter.setOnClickListener { bottomSheet() }
        }
    }

    private fun bottomSheet() {
        FilterDialog(R.layout.filter_layout, object : OnFilterDialogListener {
            override fun onApplyClicked(
                handlers: FilterDialog.MyHandlers,
                order: Boolean,
                continent: String,
                sortBy: String
            ) {
                statisticsAdapter?.let {
                    it.setList(filter(order, continent, sortBy))
                    animateList()
                }
                handlers.onClose()
            }

            private fun filter(
                order: Boolean,
                continent: String,
                sortBy: String
            ): List<ViewType<*>> {
                var list = statisticsList

                if (continent != "All")
                    list = list.filter { (it.data() as Statistics).continent == continent }

                return when {
                    order && sortBy == "Total Case" -> list.sortedBy { (it.data() as Statistics).cases.total }
                    order && sortBy == "Deaths" -> list.sortedBy { (it.data() as Statistics).deaths.total }
                    order && sortBy == "Country" -> list.sortedByDescending { (it.data() as Statistics).country }
                    order && sortBy == "Recovered" -> list.sortedBy { (it.data() as Statistics).cases.recovered }

                    !order && sortBy == "Total Case" -> list.sortedByDescending { (it.data() as Statistics).cases.total }
                    !order && sortBy == "Deaths" -> list.sortedByDescending { (it.data() as Statistics).deaths.total }
                    !order && sortBy == "Country" -> list.sortedBy { (it.data() as Statistics).country }
                    !order && sortBy == "Recovered" -> list.sortedByDescending { (it.data() as Statistics).cases.recovered }
                    else -> list
                }
            }

            override fun describeContents() = 0
            override fun writeToParcel(dest: Parcel?, flags: Int) {}

        }).show(requireActivity().supportFragmentManager, "FILTER_DIALOG")
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
                popup(getString(R.string.more), R.id.toolbar_more)
            }

            toolbarSearch.setOnLongClickListener {
                popup(getString(R.string.search), R.id.toolbar_search)
            }
        }
    }

    private fun popup(text: String, resource: Int): Boolean {
        val anchor: View = requireView().findViewById(resource)
        PopupMenu.show(text, anchor, -50, 0)
        return true
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
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
        statisticsList = ArrayList(statistics.map { StatisticsViewType(it) })
        statisticsAdapter!!.setList(statisticsList)
        animateList()
    }

    private fun animateList() {
        AnimationUtils.loadLayoutAnimation(
            requireContext(),
            R.anim.layout_animation_slide_from_bottom
        ).also { binding.statisticsList.layoutAnimation = it }
    }

    private fun countryRecycler() {
        statisticsAdapter = ViewTypeAdapter()
        binding.statisticsList.apply {
            adapter = statisticsAdapter
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