package com.todo.app.repository

import com.todo.app.data.local.WeatherDao
import com.todo.app.data.local.WeatherEntityModel
import com.todo.networkmodule.api.Api
import com.todo.networkmodule.dto.CurrentWeatherResponse
import com.todo.networkmodule.dto.FutureWeatherResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: Api,
    private val weatherDao: WeatherDao
) {
    suspend fun getCurrentWeather(query: String): Response<CurrentWeatherResponse> =
        api.getCurrentWeather(query)

    suspend fun getFutureWeather(lat: String, long: String): Response<FutureWeatherResponse> =
        api.getFutureWeather(lat, long)

    fun getSavedWeather() = weatherDao.getSavedWeather()

    suspend fun insertWeather(weatherEntityModel: WeatherEntityModel) =
        weatherDao.insert(weatherEntityModel)

    suspend fun deleteAllWeather() = weatherDao.deleteAll()
}