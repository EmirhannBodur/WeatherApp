package com.emirhan.weatherApp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emirhan.apideneme.R
import com.emirhan.weatherApp.apiService.RetrofitInstance
import com.emirhan.weatherApp.model.WeatherForecastResponse
import com.emirhan.weatherApp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WeatherViewModel(application: Application):AndroidViewModel(application) {
    private val _weather=MutableLiveData<WeatherResponse>()
    val weather:LiveData<WeatherResponse> get() = _weather
    private val _forecastWeather=MutableLiveData<WeatherForecastResponse>()
    val forecastWeather:LiveData<WeatherForecastResponse>get() =_forecastWeather

    private val apiKey:String=application.getString(R.string.apiKey)

    fun fetchForeCast(cityName: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response=RetrofitInstance.api.getForeCastWeather(cityName,apiKey)
                groupByDate(response)
            }catch (e:HttpException){
                e.printStackTrace()
            }
        }
    }
    // Veriyi tarihe göre grupla
    private fun groupByDate(response: WeatherForecastResponse){
        val groupedList = response.list.groupBy { it.dt_txt.split(" ")[0] }

        // Gruplanan verileri WeatherForecastResponse ile güncelle
        val updatedResponse = response.copy(list = groupedList.flatMap { it.value })

        // Yeni gruplanan veriyi LiveData'ya gönder
        _forecastWeather.postValue(updatedResponse)
    }



    fun fetchWeather(cityName:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response= RetrofitInstance.api.getCurrentWeather(cityName,apiKey)
                val tempCelcius= response.main!!.temp!! - 273.15
                val maxTempCelcius=response.main!!.tempMax!! - 273.15
                val roundedMaxTempCelcius=String.format("%.2f",maxTempCelcius).toDouble()
                val roundedTempCelcius=String.format("%.2f",tempCelcius).toDouble()
                val minTempCelcius=response.main!!.tempMin!! - 273.15
                val roundedMinTempCelcius=String.format("%.2f",minTempCelcius).toDouble()
                val feelsTempCelcius=response.main!!.feelsLike!! -273.15
                val roundedFeelsTempCelcius=String.format("%.2f",feelsTempCelcius).toDouble()


                val uptadeMain=response.main?.copy(
                    temp = roundedTempCelcius,
                    tempMax = roundedMaxTempCelcius,
                    tempMin = roundedMinTempCelcius,
                    feelsLike = roundedFeelsTempCelcius,
                )

                val weather_response=response.copy(
                    main = uptadeMain,

                )
                _weather.postValue(weather_response)
            }catch (e:HttpException){
                e.printStackTrace()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}