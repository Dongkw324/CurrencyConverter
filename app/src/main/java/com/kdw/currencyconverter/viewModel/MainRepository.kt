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

// 클래스의 생성자에서 @Inject Annotation을 사용하여 의존성을 생성하는 방법이다.
// 생성자 constructor()에 @Inject Annotation으로 의존성 인스턴스를 생성하고, 생성자의 파라미터로 의존성을 주입받을 수 있다.
class MainRepository @Inject constructor(private val newApiSource: NewApiSource): ApiDataSource()  {

    suspend fun getConvertData(api_key: String, from: String, to: String, amount: Double) : Flow<Resource<ApiResponse>> {
        return flow {
            emit(safeApiCall { newApiSource.getConvertRate(api_key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }
}