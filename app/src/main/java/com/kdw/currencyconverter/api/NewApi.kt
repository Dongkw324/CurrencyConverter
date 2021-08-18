package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.data.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// 수행하는데 필요한 모든 가능한 HTTP 작업 포함. 여러 매개 변수를 이용해서 API 에 GET 요청을 한다
interface NewApi {

    //GET 방식으로 데이터 받아옴. https://api.getgeoapi.com/v2/currency/convert 이라는 URI 를 호출. parameter 로 api_key, from, to, amount 를 받는다.
    @GET("convert")
    suspend fun currencyConvert(
        @Query("api_key") key : String,
        @Query("from") from : String,
        @Query("to") to : String,
        @Query("amount") amount: Double
    ) : Response<CurrencyResponse>
}