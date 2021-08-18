package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.data.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewApi {

    @GET("convert")
    suspend fun currencyConvert(
        @Query("api_key") key : String,
        @Query("from") from : String,
        @Query("to") to : String,
        @Query("amount") amount: Double
    ) : Response<CurrencyResponse>
}