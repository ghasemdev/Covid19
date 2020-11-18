package com.jakode.covid19.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FargmentSearchBinding
import com.jakode.covid19.model.SearchHistory
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.activities.SearchActivity
import com.jakode.covid19.ui.adapter.ViewType
import com.jakode.covid19.ui.adapter.ViewTypeAdapter
import com.jakode.covid19.ui.home.MainStateEvent
import com.jakode.covid19.utils.DataState
import com.jakode.covid19.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fargment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FargmentSearchBinding? = null
    private val binding get() = _binding!!

    private var statisticsAdapter: ViewTypeAdapter<ViewType<*>>? = null
    private var searchHistoryAdapter: ViewTypeAdapter<ViewType<*>>? = null
    private lateinit var searchList: ArrayList<ViewType<*>>

    private lateinit var query: String

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FargmentSearchBinding.bind(view)
        observe()
        initToolbar()
        initSearchView()
        initRecycler()
        clickListener()
    }

    private fun clickListener() {
        // Clear history
        binding.apply {
            removeAll.setOnClickListener {
                viewModel.removeAllSearch()
                searchHistoryAdapter!!.removeElements()
                historyEmpty()
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun initRecycler() {
        binding.apply {
            searchHistoryAdapter = ViewTypeAdapter(onItemActionListener =
            object : ViewTypeAdapter.OnItemHistorySearchListener<SearchHistory> {
                override fun onItemClicked(position: Int, item: SearchHistory) {
                    // Update in search history
                    viewModel.removeSearch(item)
                    searchHistoryAdapter!!.removeElement(position)

                    val search = item.apply { date = Date() }
                    viewModel.insertSearch(search)
                    searchHistoryAdapter!!.insertElement(SearchHistoryViewType(search), 0)

                    // Set in searchView
                    setQuery(item.query)
                }

                override fun onItemRemove(position: Int, item: SearchHistory) {
                    viewModel.removeSearch(item)
                    searchHistoryAdapter!!.removeElement(position)
                    if (searchHistoryAdapter!!.itemCount == 0) nothingInput()
                }
            })
            historyList.apply {
                adapter = searchHistoryAdapter
            }

            statisticsAdapter = ViewTypeAdapter(onItemActionListener =
            object : ViewTypeAdapter.OnItemSearchListener {
                override fun getQuery() = query
            })
            countryList.apply {
                adapter = statisticsAdapter
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun initSearchView() {
        binding.apply { // open search view
            search.onActionViewExpanded()

            // search view text change listener
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.search.clearFocus()
                    // Insert in search history
                    query?.let { it ->
                        if (it.isNotEmpty()) {
                            var pos: Int

                            // If query has exist updated
                            searchList
                                .map { search -> (search.data() as SearchHistory).query }
                                .also { list -> pos = list.indexOf(it) }

                            if (pos > -1) {
                                viewModel.removeSearch(searchList[pos].data() as SearchHistory)
                                searchHistoryAdapter!!.removeElement(pos)
                            }
                            val item = SearchHistory(query = it, date = Date())
                            viewModel.insertSearch(item)
                            searchHistoryAdapter!!.insertElement(SearchHistoryViewType(item), 0)
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        query = it
                        viewModel.setStateEvent(MainStateEvent.GetStatisticEvents, it)
                    }
                    return true
                }
            })

            // set search info from xml and enabled voice
            val searchManager =
                requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
            search.apply { setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName)) }
        }
    }

    private fun initToolbar() {
        // Set action for menu icon
        binding.toolbar.setNavigationOnClickListener {
            hideKeyboard(requireContext(), it)
            (activity as SearchActivity).onBackPressed()
        }
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewModel.setStateEvent(MainStateEvent.GetSearchHistoryEvents)

        viewModel.historySearchHistory.observe(viewLifecycleOwner, { dataState ->
            if (dataState is DataState.Success<List<SearchHistory>>) {
                setSearch(dataState.data)
            }
        })

        viewModel.statistics.observe(viewLifecycleOwner, { dataState ->
            if (dataState is DataState.Success<List<Statistics>> && ::query.isInitialized) {
                displayProgressBar(false)
                if (query.isNotEmpty()) {
                    if (dataState.data.isNotEmpty()) findSomething(dataState.data) else nothingFind()
                } else {
                    nothingInput()
                }
            } else if (dataState is DataState.Loading) {
                displayProgressBar(true)
            }
        })
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.apply {
            loading.visibility = if (isDisplayed) View.VISIBLE else View.GONE
            descriptionText.visibility = if (isDisplayed) View.GONE else View.VISIBLE
            titleText.visibility = if (isDisplayed) View.GONE else View.VISIBLE
            cardView.visibility = if (isDisplayed) View.GONE else View.VISIBLE
        }
    }

    private fun setSearch(searchHistories: List<SearchHistory>) {
        searchList = ArrayList(searchHistories.map { SearchHistoryViewType(it) })
        searchHistoryAdapter!!.setList(searchList)

        if (searchList.isEmpty()) {
            historyEmpty()
        } else {
            historyNotEmpty()
        }
    }

    private fun findSomething(list: List<Statistics>) {
        binding.apply {
            visibilityOnFindSomething()
            titleText.text = getString(R.string.country)
            val text = "${list.size} ${getString(R.string.country_find)}"
            descriptionText.text = text
            statisticsAdapter!!.setList(list.map { SearchViewType(it) })
        }
    }

    private fun nothingFind() {
        visibilityOnNothingFind()
        binding.errorText.text = getText(R.string.empty_search)
    }

    @ExperimentalCoroutinesApi
    private fun nothingInput() {
        binding.apply {
            historyList.visibility = View.VISIBLE
            viewModel.setStateEvent(MainStateEvent.GetSearchHistoryEvents)
            statisticsAdapter!!.setList(listOf())
        }
    }

    private fun historyEmpty() {
        binding.apply {
            titleText.visibility = View.GONE
            descriptionText.visibility = View.GONE
            removeAll.visibility = View.GONE
            errorText.visibility = View.VISIBLE
            errorText.text = getText(R.string.empty_recent_search)
        }
    }

    private fun historyNotEmpty() {
        binding.apply {
            titleText.visibility = View.VISIBLE
            descriptionText.visibility = View.GONE
            removeAll.visibility = View.VISIBLE
            errorText.visibility = View.GONE
            binding.titleText.text = getText(R.string.recent_search)
        }
    }

    private fun visibilityOnFindSomething() {
        binding.apply {
            titleText.visibility = View.VISIBLE
            descriptionText.visibility = View.VISIBLE
            errorText.visibility = View.INVISIBLE
            countryList.visibility = View.VISIBLE
            historyList.visibility = View.GONE
            removeAll.visibility = View.GONE
        }
    }

    private fun visibilityOnNothingFind() {
        binding.apply {
            titleText.visibility = View.GONE
            descriptionText.visibility = View.GONE
            errorText.visibility = View.VISIBLE
            countryList.visibility = View.GONE
            historyList.visibility = View.GONE
            removeAll.visibility = View.GONE
        }
    }

    private fun setQuery(query: String) {
        binding.search.setQuery(query, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.intent?.let { handleIntent(it) }  // Handel intent
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also {
                binding.search.setQuery(it, true)
            }
        }
    }

    override fun onDestroyView() {
        statisticsAdapter = null
        searchHistoryAdapter = null
        _binding = null
        super.onDestroyView()
    }
}