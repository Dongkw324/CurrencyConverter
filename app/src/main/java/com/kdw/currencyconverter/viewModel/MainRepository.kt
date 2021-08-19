package com.kdw.currencyconverter.viewModel

import com.kdw.currencyconverter.api.ApiResource
import com.kdw.currencyconverter.api.NewApiSource
import com.kdw.currencyconverter.data.CurrencyResponse
import com.kdw.currencyconverter.util.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(private val newApiSource: NewApiSource) : ApiResource() {

    suspend fun getConverterData(key: String, from: String, to: String, amount: Double) : Flow<ResultResponse<CurrencyResponse>> {
        return flow {
           emit(safeApiCall { newApiSource.getCurrency(key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }
}