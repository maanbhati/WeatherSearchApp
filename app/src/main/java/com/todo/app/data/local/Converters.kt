package com.todo.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun dailyEntityModelToString(dailyEntityModelList: List<DailyEntityModel>): String {
        val gson = Gson()
        val type = object : TypeToken<List<DailyEntityModel>>() {}.type
        return gson.toJson(dailyEntityModelList, type)
    }

    @TypeConverter
    fun stringToDailyEntityModel(data: String): List<DailyEntityModel> {
        val gson = Gson()
        val listType = object : TypeToken<List<DailyEntityModel>>() {}.type
        return gson.fromJson(data, listType)
    }
}