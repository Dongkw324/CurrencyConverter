package com.kdw.currencyconverter.viewModel

import com.kdw.currencyconverter.api.ApiDataSource
import com.kdw.currencyconverter.api.NewApiSource
import com.kdw.currencyconverter.model.ApiResponse
import com.kdw.currencyconverter.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(private val newApiSource: NewApiSource): ApiDataSource()  {

    suspend fun getConvertData(api_key: String, from: String, to: String, amount: Double) : Flow<Resource<ApiResponse>> {
        return flow {
            emit(safeApiCall { newApiSource.getConvertRate(api_key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }
}