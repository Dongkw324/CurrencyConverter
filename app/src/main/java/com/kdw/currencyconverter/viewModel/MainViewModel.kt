package com.kdw.currencyconverter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdw.currencyconverter.model.ApiResponse
import com.kdw.currencyconverter.util.Resource
import com.kdw.currencyconverter.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

//ViewModel에서 @HildViewModel Annotation과 @Inject Annotation을 사용하여 간단하게 ViewModel 의존성 주입 활성화 가능
@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

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