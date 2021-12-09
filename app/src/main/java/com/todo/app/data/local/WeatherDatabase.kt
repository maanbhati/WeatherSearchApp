package com.todo.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [WeatherEntityModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    class Callback @Inject constructor(
        private val database: Provider<WeatherDatabase>
    ) : RoomDatabase.Callback()
}