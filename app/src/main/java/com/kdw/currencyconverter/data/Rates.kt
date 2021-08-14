package com.kdw.currencyconverter.data


import com.google.gson.annotations.SerializedName

data class Rates(
    @SerializedName("currency_name")
    val currencyName: String,
    val rate: String,
    @SerializedName("rate_for_amount")
    val rateForAmount: Double
)