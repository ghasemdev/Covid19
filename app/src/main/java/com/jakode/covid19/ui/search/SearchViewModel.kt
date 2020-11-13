package com.jakode.covid19.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jakode.covid19.data.AppRepository
import com.jakode.covid19.model.SearchHistory
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.home.MainStateEvent
import com.jakode.covid19.utils.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _historySearchHistory: MutableLiveData<DataState<List<SearchHistory>>> by lazy { MutableLiveData() }
    val historySearchHistory: LiveData<DataState<List<SearchHistory>>> get() = _historySearchHistory

    private val _statistics: MutableLiveData<DataState<List<Statistics>>> by lazy { MutableLiveData() }
    val statistics: LiveData<DataState<List<Statistics>>> get() = _statistics

    @ExperimentalCoroutinesApi
    fun setStateEvent(mainStateEvent: MainStateEvent, query: String = "") {
        viewModelScope.launch(IO) {
            when (mainStateEvent) {
                is MainStateEvent.GetSearchHistoryEvents -> {
                    appRepository.getHistorySearch().onEach { data ->
                        _historySearchHistory.value = data
                    }.launchIn(viewModelScope)
                }
                is MainStateEvent.GetStatisticEvents -> {
                    appRepository.countrySearch(query).onEach { data ->
                        _statistics.value = data
                    }.launchIn(viewModelScope)
                }
                else -> {}
            }
        }
    }

    fun removeAllSearch() {
        viewModelScope.launch(IO) {
            appRepository.removeAllSearch()
        }
    }

    fun removeSearch(searchHistory: SearchHistory) {
        viewModelScope.launch(IO) {
            appRepository.removeSearch(searchHistory)
        }
    }

    fun insertSearch(searchHistory: SearchHistory) {
        viewModelScope.launch(IO) {
            appRepository.insertSearch(searchHistory)
        }
    }
}