package com.kdw.currencyconverter.data


import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    val amount: Int,
    @SerializedName("base_currency_code")
    val baseCurrencyCode: String,
    @SerializedName("base_currency_name")
    val baseCurrencyName: String,
    val rates: Rates,
    val status: String,
    @SerializedName("updated_date")
    val updatedDate: String
)