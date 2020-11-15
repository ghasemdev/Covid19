package com.jakode.covid19.data

import com.jakode.covid19.data.api.CovidApi
import com.jakode.covid19.data.api.mapper.NetworkMapper
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.SearchDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import com.jakode.covid19.data.database.mapper.CacheMapper
import com.jakode.covid19.data.datastore.DataStoreRepository
import com.jakode.covid19.model.SearchHistory
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.model.StatisticsAndGlobal
import com.jakode.covid19.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val statisticsDao: StatisticsDao,
    private val globalDao: GlobalDao,
    private val searchDao: SearchDao,
    private val api: CovidApi,
    private val dataStore: DataStoreRepository,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {
    private val sameContinent: Predicate<Statistics> = { it.country != it.continent }
    private val unknownCountry: Predicate<Statistics> =
        { it.country != "Cura&ccedil;ao" && it.country != "R&eacute;union" }

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

    suspend fun getStatisticsAndGlobal(isRefreshed: Boolean): Flow<DataState<StatisticsAndGlobal>> = flow {
        emit(DataState.Loading)
        checkCacheDuration()
        val updateTime = dataStore.readTime.first()
        if (updateTime != 0L && System.nanoTime() - updateTime < refreshTime && !isRefreshed) {
            // Fetch from database
            val cacheStatistics = statisticsDao.getAll()
            val statistics = cacheMapper.mapFromEntityList(cacheStatistics)

            val cacheGlobal = globalDao.get()
            val global = cacheMapper.mapFromGlobal(cacheGlobal)

            // emit
            val globalAndStatistics = StatisticsAndGlobal(global, statistics.subList(0, 10))
            emit(DataState.Success(globalAndStatistics))
        } else {
            // Fetch from remote
            try {
                val networkStatistics = api.getStatistics().statistics
                val statistics = statisticsDesSort(
                    networkMapper.mapFromEntityList(networkStatistics),
                    unknownCountry
                )
                val global = cacheMapper.statisticToGlobal(statistics[0])

                // Store locally
                statisticsDao.insert(*cacheMapper.mapToEntityList(statistics).toTypedArray())
                globalDao.insert(cacheMapper.mapToGlobal(global))
                dataStore.updateTime(System.nanoTime())

                // emit success
                val globalAndStatistics = StatisticsAndGlobal(
                    global,
                    statisticsFilter(statistics, sameContinent).subList(0, 10)
                )
                emit(DataState.Success(globalAndStatistics))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
    }

    suspend fun getStatistics(): Flow<DataState<List<Statistics>>> = flow {
        emit(DataState.Loading)
        checkCacheDuration()
        val updateTime = dataStore.readTime.first()
        if (updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            // Fetch from database
            val cacheStatistics = statisticsDao.getAll()
            val statistics = cacheMapper.mapFromEntityList(cacheStatistics)

            // emit
            emit(DataState.Success(statistics))
        } else {
            // Fetch from remote
            try {
                val networkStatistics = api.getStatistics().statistics
                val statistics = statisticsDesSort(
                    networkMapper.mapFromEntityList(networkStatistics),
                    unknownCountry
                )

                // Store locally
                statisticsDao.insert(*cacheMapper.mapToEntityList(statistics).toTypedArray())
                dataStore.updateTime(System.nanoTime())

                // emit success
                emit(DataState.Success(statisticsFilter(statistics, sameContinent)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
    }

    suspend fun removeAllSearch() = searchDao.deleteAll()
    suspend fun removeSearch(searchHistory: SearchHistory) =
        searchDao.delete(cacheMapper.mapToSearch(searchHistory))

    suspend fun insertSearch(searchHistory: SearchHistory) {
        searchDao.getByQuery(searchHistory.query)?.let {
            removeSearch(cacheMapper.mapFromSearch(it))
        }
        searchDao.insert(cacheMapper.mapToSearch(searchHistory))
    }

    suspend fun getHistorySearch(): Flow<DataState<List<SearchHistory>>> = flow {
        val cacheSearch = searchDao.getAll()
        val searches = cacheMapper.mapFromSearchList(cacheSearch).sortedByDescending { it.date }

        emit(DataState.Success(searches))
    }

    suspend fun countrySearch(query: String): Flow<DataState<List<Statistics>>> = flow {
        emit(DataState.Loading)
        if (isPersian()) {
            val searchStatistics = CountryRename.search(query)

            val cacheStatistics = statisticsDao.getAll()
            val allStatistics = cacheMapper.mapFromEntityList(cacheStatistics)

            val statistics = ArrayList<Statistics>()
            for (country in searchStatistics) {
                for (statistic in allStatistics) {
                    if (country == statistic.country) {
                        statistics.add(statistic)
                        break
                    }
                }
            }
            emit(DataState.Success(statistics))
        } else {
            val country = "%${query}%"
            val cacheStatistics = statisticsDao.getStatisticByCountry(country)
            val statistics = cacheMapper.mapFromEntityList(cacheStatistics)
            delay(250)
            emit(DataState.Success(statisticsFilter(statistics, sameContinent)))
        }
    }
}