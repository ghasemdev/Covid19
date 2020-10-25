package com.jakode.covid19.ui.intro

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakode.covid19.data.datastore.DataStoreRepository
import kotlinx.coroutines.launch

class IntroViewModel @ViewModelInject constructor(
    private val dataStore: DataStoreRepository
) : ViewModel() {

    fun saveState(state: Boolean) = viewModelScope.launch {
        dataStore.saveState(state)
    }
}