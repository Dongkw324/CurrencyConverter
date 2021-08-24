package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.util.Resource
import retrofit2.Response

abstract class ApiDataSource {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>) : Resource<T> {
        try{
            val res = apiCall()
            if(res.isSuccessful) {
                val body = res.body()
                if(body != null) {
                    return Resource.success(body)
                }
            }
            return Resource.error("네트워크 연결 오류 : ${res.code()} ${res.message()}")
        }catch(e: Exception) {
            return Resource.error(e.message ?: e.toString())
        }
    }

}