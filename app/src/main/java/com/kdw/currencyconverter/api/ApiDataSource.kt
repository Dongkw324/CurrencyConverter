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
            return error("${res.code()} ${res.message()}")
        }catch(e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String) : Resource<T> {
        return Resource.error("Network call failed : $message")
    }
}