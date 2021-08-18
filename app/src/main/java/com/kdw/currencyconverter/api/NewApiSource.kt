package com.kdw.currencyconverter.api

import javax.inject.Inject

class NewApiSource @Inject constructor(private val newApi: NewApi) {

    suspend fun getCurrency(key: String, from: String, to: String, amount: Double) {
        newApi.currencyConvert(key, from, to, amount)
    }
}