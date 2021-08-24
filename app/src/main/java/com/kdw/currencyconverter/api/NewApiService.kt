package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


// API interface 만들기
// @GET 방식 이용, 쿼리 값으로는 api_key, from, to, amount 를 이용
// ex) https://api.getgeoapi.com/v2/currency/convert?api_key=API_KEY&from=USD&to=KRW&amount=1&format=json
// 미국 1달러를 우리나라 돈으로 얼마인지 확인하는 정보
interface NewApiService {

    @GET("convert")
    suspend fun convertCurrency(
        @Query("api_key") api_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ) : Response<ApiResponse>
}