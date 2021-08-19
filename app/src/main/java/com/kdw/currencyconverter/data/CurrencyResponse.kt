package com.kdw.currencyconverter.data


data class CurrencyResponse(
    val amount: Int,
    val baseCurrencyCode: String,
    val baseCurrencyName: String,
    val rates: HashMap<String, Rates> = HashMap(),
    val status: String,
    val updatedDate: String
)