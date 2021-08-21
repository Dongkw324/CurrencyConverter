package com.kdw.currencyconverter.api

import javax.inject.Inject

class NewApiSource @Inject constructor(private val newApiService: NewApiService){

    suspend fun getConvertRate(api_key: String, from: String, to: String, amount: Double) =
        newApiService.convertCurrency(api_key, from, to, amount)

}