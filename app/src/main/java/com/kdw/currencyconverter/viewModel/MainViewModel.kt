package com.kdw.currencyconverter.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdw.currencyconverter.data.CurrencyResponse
import com.kdw.currencyconverter.util.ResultResponse
import com.kdw.currencyconverter.util.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _data = SingleLiveEvent<ResultResponse<CurrencyResponse>>()

    val data = _data
    val convertedRated = MutableLiveData<Double>()

    fun getConverterData(key: String, from: String, to: String, amount: Double) {
        viewModelScope.launch {
            mainRepository.getConverterData(key, from, to, amount).collect {
                data.value = it
            }
        }
    }
}