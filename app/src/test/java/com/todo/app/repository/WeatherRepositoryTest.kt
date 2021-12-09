package com.todo.app.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.todo.app.data.local.WeatherDao
import com.todo.app.data.local.WeatherEntityModel
import com.todo.networkmodule.api.Api
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var api: Api

    @MockK
    private lateinit var weatherDao: WeatherDao

    @MockK
    private lateinit var weatherEntityModel: WeatherEntityModel

    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        repository = WeatherRepository(api, weatherDao)
    }

    @Test
    fun when_get_current_weather_called_verify_method_call_from_api() = runBlockingTest {
        val query = "query"
        repository.getCurrentWeather(query)

        verify { runBlockingTest { api.getCurrentWeather(query) } }
    }

    @Test
    fun when_get_future_weather_called_verify_method_call_from_api() = runBlockingTest {
        val lat = "12345"
        val long = "67892"
        repository.getFutureWeather(lat, long)

        verify { runBlockingTest { api.getFutureWeather(lat, long) } }
    }

    @Test
    fun when_get_saved_weather_called_verify_method_call_from_dao() {
        repository.getSavedWeather()

        verify { weatherDao.getSavedWeather() }
    }

    @Test
    fun when_insert_weather_called_verify_method_call_from_dao() = runBlockingTest {
        repository.insertWeather(weatherEntityModel)

        verify { runBlockingTest { weatherDao.insert(weatherEntityModel) } }
    }

    @Test
    fun when_delete_weather_called_verify_method_call_from_dao() = runBlockingTest {
        repository.deleteAllWeather()

        verify { runBlockingTest { weatherDao.deleteAll() } }
    }
}