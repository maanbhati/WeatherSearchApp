package com.todo.app.data.helper

import com.todo.app.data.local.DailyEntityModel
import com.todo.app.data.local.WeatherEntityModel
import com.todo.app.data.remote.DailyDomainModel
import com.todo.app.data.remote.WeatherDomainModel
import com.todo.networkmodule.dto.CurrentWeatherResponse
import com.todo.networkmodule.dto.FutureWeatherResponse
import javax.inject.Inject

class ModelConverter @Inject constructor() {

    fun convertFromDomainToEntityModel(
        currentWeatherResponse: CurrentWeatherResponse,
        futureWeatherResponse: FutureWeatherResponse
    ): WeatherEntityModel {
        return WeatherEntityModel(
            currentWeatherResponse.id,
            currentWeatherResponse.dt,
            currentWeatherResponse.name,
            currentWeatherResponse.main.temp,
            currentWeatherResponse.main.temp_min,
            currentWeatherResponse.main.temp_max,
            currentWeatherResponse.weather[0].description,
            currentWeatherResponse.weather[0].icon,
            daily = futureWeatherResponse.daily.map { DailyEntityModel(it) }
        )
    }

    fun convertEntityToDomainModel(
        weatherEntityModel: WeatherEntityModel
    ): WeatherDomainModel {
        return WeatherDomainModel(
            weatherEntityModel.id,
            weatherEntityModel.dt,
            weatherEntityModel.name,
            weatherEntityModel.temp,
            weatherEntityModel.temp_min,
            weatherEntityModel.temp_max,
            weatherEntityModel.description,
            weatherEntityModel.icon,
            daily = weatherEntityModel.daily.map { DailyDomainModel(it) }
        )
    }
}