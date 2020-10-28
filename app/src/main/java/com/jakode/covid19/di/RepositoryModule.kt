package com.jakode.covid19.di

import com.jakode.covid19.data.AppRepository
import com.jakode.covid19.data.api.CovidApi
import com.jakode.covid19.data.api.mapper.NetworkMapper
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import com.jakode.covid19.data.database.mapper.CacheMapper
import com.jakode.covid19.data.datastore.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        statisticsDao: StatisticsDao,
        globalDao: GlobalDao,
        api: CovidApi,
        dataStore: DataStoreRepository,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): AppRepository {
        return AppRepository(statisticsDao, globalDao, api, dataStore, cacheMapper, networkMapper)
    }
}