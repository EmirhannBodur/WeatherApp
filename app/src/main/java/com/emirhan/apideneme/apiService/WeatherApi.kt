package com.emirhan.apideneme.apiService

import com.emirhan.apideneme.model.WeatherForecastResponse
import com.emirhan.apideneme.model.WeatherResponse
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