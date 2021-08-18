package com.kdw.currencyconverter.api

import javax.inject.Inject

//나중에 만들 Repository 에서 사용 할 수 있도록 인터페이스 노출 작업
class NewApiSource @Inject constructor(private val newApi: NewApi) {

    suspend fun getCurrency(key: String, from: String, to: String, amount: Double) {
        newApi.currencyConvert(key, from, to, amount)
    }
}