package com.kdw.currencyconverter.util

data class Resource <out T>(val status: Status, val data: T?, val msg: String?) {

    enum class Status{
        SUCCESS,
        ERROR,
    }

    companion object{
        fun<T> success(data: T) : Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun<T> error(message: String): Resource<T> {
            return Resource(Status.ERROR, null, message)
        }

    }
}