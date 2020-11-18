package com.jakode.covid19.ui.settings

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jakode.covid19.data.datastore.DataStoreRepository
import com.jakode.covid19.data.sharedpreferences.SharedPreferencesHelper
import com.jakode.covid19.utils.Language
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val dataStore: DataStoreRepository,
    private val sharedPreferences: SharedPreferencesHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun saveLanguage(language: Language) {
        viewModelScope.launch(IO) {
            sharedPreferences.language = language
        }
    }
}