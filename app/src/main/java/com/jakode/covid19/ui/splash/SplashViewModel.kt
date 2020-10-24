package com.jakode.covid19.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jakode.covid19.data.datastore.DataStoreRepository

class SplashViewModel @ViewModelInject constructor(dataStore: DataStoreRepository) : ViewModel() {
    val state = dataStore.readState.asLiveData()
}