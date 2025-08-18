package com


import android.util.Log
import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.Constant
import com.api.NetworkResponse

import com.api.RetrofitInstance
import com.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult
    fun getData(city : String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try{
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {

                        // This will show you the HTTP error code and message
                        Log.e("API_CALL", "HTTP Error: ${response.code()}, ${response.message()}")
                        _weatherResult.value = NetworkResponse.Error("Failed to load data: ${response.code()}")
                    }



            }
            catch(e : Exception) {
                    // This will show you the exact error, like "UnknownHostException"
                    Log.e("API_CALL", "Failed to fetch data: ${e.message}")
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")

            }
        }
    }
}