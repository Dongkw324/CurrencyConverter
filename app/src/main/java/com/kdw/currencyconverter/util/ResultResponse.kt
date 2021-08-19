package com.kdw.currencyconverter.util


//통신 응답 Result 클래스로 해놓음
//통신 성공시 Result.Success<T>를 반환, 실패하면 Result.Error를 반환
data class ResultResponse <out T> (val status: Status, val data: T?, val msg: String?) {
    enum class Status{
        SUCCESS,
        ERROR,
    }

    companion object{
        fun<T> success(data: T): ResultResponse<T> {
            return ResultResponse(Status.SUCCESS, data, null)
        }

        fun<T> error(msg: String) : ResultResponse<T> {
            return ResultResponse(Status.ERROR, null, msg)
        }
    }
}
