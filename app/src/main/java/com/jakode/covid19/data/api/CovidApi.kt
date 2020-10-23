package com.jakode.covid19.data.api

import com.jakode.covid19.data.api.model.GlobalResponse
import com.jakode.covid19.data.api.model.StatisticsResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface CovidApi {

    @Headers("x-rapidapi-host: covid-193.p.rapidapi.com", "x-rapidapi-key: b1dca9cd32msha4a6cb59653fc04p1c1d93jsnbfbaac9d5ecc")
    @GET("statistics")
    suspend fun getStatistics(): StatisticsResponse

    @Headers("x-rapidapi-host: covid-19-coronavirus-statistics.p.rapidapi.com", "x-rapidapi-key: b1dca9cd32msha4a6cb59653fc04p1c1d93jsnbfbaac9d5ecc")
    @GET("v1/total")
    suspend fun getGlobal(): GlobalResponse
}