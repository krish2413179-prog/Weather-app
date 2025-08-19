package com

import WeatherApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.api.NetworkResponse
import com.api.RetrofitInstance
import com.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi: WeatherApi = RetrofitInstance.api
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city : String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try{
               
                val response = weatherApi.getWeather(
                    apiKey = "c2b4c4ca452049c48fc133541251808",
                    city = city,
                    days = 7 
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data: ${response.message()}")
                }
            }
            catch(e : Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to load data: ${e.message}")
            }
        }
    }
}
