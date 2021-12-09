package com.todo.app.data.remote

import com.todo.app.data.local.DailyEntityModel
import com.todo.app.data.local.TempEntityModel
import com.todo.app.data.local.WeatherEntity
import kotlinx.parcelize.RawValue

data class WeatherDomainModel(
    val id: Int,
    val dt: Long,
    val name: String,
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val description: String,
    val icon: String,
    val daily: @RawValue List<DailyDomainModel>
)

data class DailyDomainModel(
    val dt: Long,
    val temp: TempDomainModel,
    val weather: @RawValue List<WeatherDomain>,
) {
    constructor(dailyEntityModel: DailyEntityModel) : this(
        dt = dailyEntityModel.dt,
        temp = TempDomainModel(dailyEntityModel.temp),
        weather = dailyEntityModel.weather.map { WeatherDomain(it) }
    )
}

data class TempDomainModel(
    val day: Double,
    val min: Double,
    val max: Double,
) {
    constructor(temp: TempEntityModel) : this(
        day = temp.day,
        min = temp.min,
        max = temp.max
    )
}

data class WeatherDomain(
    val description: String,
    val icon: String
) {
    constructor(weather: WeatherEntity) : this(
        description = weather.description,
        icon = weather.icon
    )
}
