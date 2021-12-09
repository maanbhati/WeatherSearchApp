package com.todo.app.di

import android.app.Application
import androidx.room.Room
import com.todo.app.data.local.WeatherDao
import com.todo.app.data.local.WeatherDatabase
import com.todo.app.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        callback: WeatherDatabase.Callback
    ): WeatherDatabase {
        return Room.databaseBuilder(application, WeatherDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao {
        return db.getWeatherDao()
    }
}