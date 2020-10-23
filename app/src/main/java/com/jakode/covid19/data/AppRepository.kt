package com.jakode.covid19.data

import com.jakode.covid19.data.api.CovidApi
import com.jakode.covid19.data.api.mapper.NetworkMapper
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import com.jakode.covid19.data.database.mapper.CacheMapper
import com.jakode.covid19.model.Global
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository(
        private val statisticsDao: StatisticsDao,
        private val globalDao: GlobalDao,
        private val api: CovidApi,
        private val cacheMapper: CacheMapper,
        private val networkMapper: NetworkMapper
) {
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

    suspend fun getGlobal():Flow<DataState<Global>> = flow {
        emit(DataState.Loading)
        try {
            val networkGlobal = api.getGlobal().global
            val global = networkMapper.mapFromGlobal(networkGlobal)
            globalDao.insert(cacheMapper.mapToGlobal(global))
//            val cacheStatistics = statisticsDao.getAll()
            emit(DataState.Success(global))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}