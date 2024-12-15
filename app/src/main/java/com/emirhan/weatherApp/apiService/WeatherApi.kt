package com.emirhan.weatherApp.apiService

import com.emirhan.weatherApp.model.WeatherForecastResponse
import com.emirhan.weatherApp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName:String,
        @Query("appid") apiKey:String
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForeCastWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units:String="metric"
    ): WeatherForecastResponse
}