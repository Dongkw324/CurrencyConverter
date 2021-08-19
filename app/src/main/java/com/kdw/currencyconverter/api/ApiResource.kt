package com.kdw.currencyconverter.api

import com.kdw.currencyconverter.util.ResultResponse
import retrofit2.Response

// 요청 상태(성공, 실패)를 처리하는데 도움이 된다. 적절한 작업을 수행하거나 적절한 오류 메시지 표시
abstract class ApiResource {

    suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>) : ResultResponse<T> {
        return try{
            val myResponse = call()
            if(myResponse.isSuccessful) {
                val myBody = myResponse.body()
                if(myBody != null) {
                    ResultResponse.success(myBody)
                }
            }
            ResultResponse.error("Something error: ${myResponse.message()}")
        }catch(e: Exception) {
            ResultResponse.error(e.message ?: e.toString())
        }
    }
}