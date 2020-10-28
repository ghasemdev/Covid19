package com.jakode.covid19.di

import android.content.Context
import androidx.room.Room
import com.jakode.covid19.data.database.AppDatabase
import com.jakode.covid19.data.database.dao.GlobalDao
import com.jakode.covid19.data.database.dao.StatisticsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideStatistics(appDataBase: AppDatabase): StatisticsDao = appDataBase.statisticsDao()

    @Singleton
    @Provides
    fun provideGlobal(appDataBase: AppDatabase): GlobalDao = appDataBase.globalDao()
}