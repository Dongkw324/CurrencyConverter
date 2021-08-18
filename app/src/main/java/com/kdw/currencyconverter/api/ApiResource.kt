package com.kdw.currencyconverter.api

import retrofit2.Response

// 요청 상태(성공, 실패)를 처리하는데 도움이 된다. 적절한 작업을 수행하거나 적절한 오류 메시지 표시
class ApiResource {

    suspend fun <T: Any> safeApiCall(call: suspend() -> Response<T>) : Result<T> {
        return try{
            val myResponse = call.invoke()
            if(myResponse.isSuccessful) {
                Result.Success(myResponse.body()!!)
            } else {
                Result.Error(myResponse.message() ?: "Something wrong")
            }

        }catch(e: Exception) {
            Result.Error(e.message ?: "Internet error")
        }
    }
}