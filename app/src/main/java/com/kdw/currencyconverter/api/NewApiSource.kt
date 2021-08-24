package com.kdw.currencyconverter.api

import javax.inject.Inject

//MainRepository 에서 사용 가능하도록 NewApiService Interface 의 convertCurrency 라는 함수를 노출시킨다.
class NewApiSource @Inject constructor(private val newApiService: NewApiService){

    suspend fun getConvertRate(api_key: String, from: String, to: String, amount: Double) =
        newApiService.convertCurrency(api_key, from, to, amount)

}