package com.jakode.covid19.data

import com.jakode.covid19.data.api.CovidApi
import com.jakode.covid19.data.api.mapper.NetworkMapper
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import com.jakode.covid19.data.database.mapper.CacheMapper
import com.jakode.covid19.data.datastore.DataStoreRepository
import com.jakode.covid19.model.Global
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val statisticsDao: StatisticsDao,
    private val globalDao: GlobalDao,
    private val api: CovidApi,
    private val dataStore: DataStoreRepository,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {
    private var refreshTime: Long = 0

    private suspend fun checkCacheDuration() {
        // Setting for duration of updating
        val cachePreference = dataStore.readDuration.first()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (exception: NumberFormatException) {
            exception.stackTrace
        }
    }

    suspend fun getGlobal(isRefreshed: Boolean): Flow<DataState<Global>> = flow {
        checkCacheDuration()
        val updateTime = dataStore.readTime.first()
        if (updateTime != 0L && System.nanoTime() - updateTime < refreshTime && !isRefreshed) {
            // Fetch from database
            val cacheGlobal = globalDao.get()
            val global = cacheMapper.mapFromGlobal(cacheGlobal)

            emit(DataState.Success(global))
        } else {
            emit(DataState.Loading)
            // Fetch from remote
            try {
                val networkGlobal = api.getGlobal().global
                val global = networkMapper.mapFromGlobal(networkGlobal)
                globalDao.insert(cacheMapper.mapToGlobal(global)) // Store locally
                dataStore.updateTime(System.nanoTime()) // Store time

                emit(DataState.Success(global))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
    }

    suspend fun getStatistics(): Flow<DataState<List<Statistics>>> = flow {
        emit(DataState.Loading)
        try {
            val networkStatistics = api.getStatistics().statistics
            val statistics = networkMapper.mapFromEntityList(networkStatistics)
            statistics.forEach { statisticsDao.insert(cacheMapper.mapToEntity(it)) }
//            val cacheStatistics = statisticsDao.getAll()
            emit(DataState.Success(statistics))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}