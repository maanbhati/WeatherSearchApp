package com.todo.app.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.todo.app.utils.TABLE_NAME
import com.todo.networkmodule.dto.Daily
import com.todo.networkmodule.dto.Temp
import com.todo.networkmodule.dto.Weather
import kotlinx.parcelize.RawValue

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = ["name", "temp"], unique = true)]
)
data class WeatherEntityModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "dt")
    val dt: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "temp")
    val temp: Double,
    @ColumnInfo(name = "temp_min")
    val temp_min: Double,
    @ColumnInfo(name = "temp_max")
    val temp_max: Double,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "daily")
    val daily: @RawValue List<DailyEntityModel>
)

data class DailyEntityModel(
    val dt: Long,
    val temp: TempEntityModel,
    val weather: @RawValue List<WeatherEntity>,
) {
    constructor(dailyDto: Daily) : this(
        dt = dailyDto.dt,
        temp = TempEntityModel(dailyDto.temp),
        weather = dailyDto.weather.map { WeatherEntity(it) }
    )
}

data class TempEntityModel(
    val day: Double,
    val min: Double,
    val max: Double,
) {
    constructor(temp: Temp) : this(
        day = temp.day,
        min = temp.min,
        max = temp.max
    )
}

data class WeatherEntity(
    val description: String,
    val icon: String
) {
    constructor(weather: Weather) : this(
        description = weather.description,
        icon = weather.icon
    )
}
