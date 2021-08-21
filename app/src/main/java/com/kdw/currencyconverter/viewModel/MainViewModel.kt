package com.kdw.currencyconverter.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdw.currencyconverter.model.ApiResponse
import com.kdw.currencyconverter.util.Resource
import com.kdw.currencyconverter.util.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository): ViewModel() {

    private val _data = SingleLiveEvent<Resource<ApiResponse>>()

    val data = _data
    val convertedRate = MutableLiveData<Double>()

    fun getConvertData(api_key: String, from: String, to: String, amount: Double) {
        viewModelScope.launch {
            mainRepository.getConvertData(api_key, from, to, amount).collect {
                data.value = it
            }
        }
    }
}