package com.jakode.covid19.di

import com.jakode.covid19.data.api.CovidApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {
    private const val BASE_URL = "https://rapidapi.p.rapidapi.com/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideCovidService(retrofit: Retrofit.Builder): CovidApi {
        return retrofit.build().create(CovidApi::class.java)
    }
}