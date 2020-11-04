package com.jakode.covid19.ui.statistics

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jakode.covid19.data.AppRepository
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.home.MainStateEvent
import com.jakode.covid19.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class StatisticViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<Statistics>>> by lazy { MutableLiveData() }
    val dataState: LiveData<DataState<List<Statistics>>> get() = _dataState

    @ExperimentalCoroutinesApi
    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (mainStateEvent) {
                is MainStateEvent.GetBlogEvents -> {
                    appRepository.getStatistics().onEach { data ->
                        _dataState.value = data
                    }.launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {
                }
            }
        }
    }
}