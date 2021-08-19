package com.kdw.currencyconverter.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdw.currencyconverter.data.CurrencyResponse
import com.kdw.currencyconverter.util.SingleLiveEvent

class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _data = SingleLiveEvent<Result<CurrencyResponse>>()

    val data = _data
    val convertedRated = MutableLiveData<Double>()

}