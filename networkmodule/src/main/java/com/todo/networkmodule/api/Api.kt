package com.todo.networkmodule.api

import com.todo.networkmodule.dto.CurrentWeatherResponse
import com.todo.networkmodule.dto.FutureWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") query: String,
        @Query("units") units: String = UNIT_METRIC,
        @Query("appid") appId: String = APP_ID
    ): Response<CurrentWeatherResponse>

    @GET("onecall")
    suspend fun getFutureWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = EXCLUDE,
        @Query("units") units: String = UNIT_METRIC,
        @Query("appid") appId: String = APP_ID
    ): Response<FutureWeatherResponse>

    companion object {
        const val EXCLUDE = "current,minutely,hourly"
        const val UNIT_METRIC = "metric"
        const val APP_ID = "<PLACE_API_KEY_HERE>"
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val FORECAST_ICON = BASE_URL + "img/w/"
    }
}