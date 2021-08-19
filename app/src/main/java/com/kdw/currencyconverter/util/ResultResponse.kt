package com.kdw.currencyconverter.util


//통신 응답 Result 클래스로 해놓음
//통신 성공시 Result.Success<T>를 반환, 실패하면 Result.Error를 반환
sealed class ResultResponse<out T: Any> {
    data class Success<out T: Any>(val data: T) : ResultResponse<T>()
    data class Error(val message: String) : ResultResponse<Nothing>()
}
