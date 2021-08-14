package com.kdw.currencyconverter.util

import com.kdw.currencyconverter.BuildConfig


class Constants {

    companion object{
        const val BASE_URL = "https://api.getgeoapi.com/v2/currency/convert"
        const val API_KEY = BuildConfig.CURRENCY_API_ID

    }
}