package com.todo.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.todo.app.utils.TABLE_NAME

@Dao
interface WeatherDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getSavedWeather(): WeatherEntityModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherEntityModel: WeatherEntityModel): Long

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}