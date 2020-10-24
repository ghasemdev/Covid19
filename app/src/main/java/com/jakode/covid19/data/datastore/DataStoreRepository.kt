package com.jakode.covid19.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREFERENCE_NAME = "covid_preference"

class DataStoreRepository(context: Context) {

    private object PreferenceKey {
        val state = preferencesKey<Boolean>("mode")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    val readState: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DATA_STORE", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val state = preference[PreferenceKey.state] ?: false
            state
        }

    suspend fun saveState(state: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKey.state] = state
        }
    }
}