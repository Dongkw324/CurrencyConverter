package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.util.Resource
import retrofit2.Response

//응답 관리 함수
abstract class ApiDataSource {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>) : Resource<T> {
        try{
            val res = apiCall()
            // 응답 정상적으로 받았을 때
            if(res.isSuccessful) {
                val body = res.body()
                if(body != null) {
                    return Resource.success(body)
                }
            }
            // 응답이 실패했을 때
            return Resource.error("네트워크 연결 오류 : ${res.code()} ${res.message()}")
        }catch(e: Exception) {
            return Resource.error(e.message ?: e.toString())
        }
    }

}