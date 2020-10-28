package com.jakode.covid19.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jakode.covid19.data.AppRepository
import com.jakode.covid19.model.Global
import com.jakode.covid19.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<Global>> by lazy { MutableLiveData() }

    val dataState: LiveData<DataState<Global>>
        get() = _dataState

    @ExperimentalCoroutinesApi
    fun setStateEvent(mainStateEvent: MainStateEvent, isRefreshed: Boolean) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetBlogEvents -> {
                    appRepository.getGlobal(isRefreshed).onEach { dataState ->
                        _dataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {
                }
            }
        }
    }
}

sealed class MainStateEvent {
    object GetBlogEvents : MainStateEvent()
    object None : MainStateEvent()
}