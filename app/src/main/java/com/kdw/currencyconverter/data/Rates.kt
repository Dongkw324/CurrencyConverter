package com.kdw.currencyconverter.data


data class Rates(
    val currencyName: String,
    val rate: String,
    val rateForAmount: Double
)