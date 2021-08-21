package com.kdw.currencyconverter.model

data class ApiResponse(
    val amount: String,
    val base_currency_code: String,
    val base_currency_name: String,
    var rates: HashMap<String, Rates> = HashMap(),
    var status: String,
    var updated_date: String
)
