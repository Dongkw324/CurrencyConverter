package com.kdw.currencyconverter.util

import com.kdw.currencyconverter.BuildConfig

class Constants {

    companion object {
        const val BASE_URL = "https://api.getgeoapi.com/api/v2/currency/"

        const val API_KEY = BuildConfig.CURRENCY_API_ID

        const val CONVERT = "convert"
    }
}