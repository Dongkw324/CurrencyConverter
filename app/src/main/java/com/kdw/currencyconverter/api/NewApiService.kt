package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.model.ApiResponse
import com.kdw.currencyconverter.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewApiService {

    @GET(Constants.CONVERT)
    suspend fun convertCurrency(
        @Query("api_key") api_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ) : Response<ApiResponse>
}