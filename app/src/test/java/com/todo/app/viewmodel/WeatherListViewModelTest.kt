package com.todo.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.todo.app.data.helper.ModelConverter
import com.todo.app.data.local.WeatherEntityModel
import com.todo.app.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: WeatherRepository

    @MockK
    private lateinit var modelConverter: ModelConverter

    @MockK
    private lateinit var weatherDomainEntityModel: WeatherEntityModel

    private lateinit var viewModel: WeatherListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = WeatherListViewModel(repository, modelConverter)
    }

    @Test
    fun when_get_saved_weather_called_verify_method_call_from_repository() = runBlockingTest {
        viewModel.getSavedWeather()

        verify { repository.getSavedWeather() }
    }

    @Test
    fun when_search_weather_called_verify_method_call_from_repository() = runBlockingTest {
        val query = "query"
        viewModel.searchWeather(query)

        verify { runBlockingTest { repository.getCurrentWeather(query) } }
    }

    @Test
    fun when_update_weather_called_verify_method_call_from_repository() = runBlockingTest {
        viewModel.updateWeatherData(weatherDomainEntityModel)

        verify { runBlockingTest { repository.deleteAllWeather() } }
        verify { runBlockingTest { repository.insertWeather(weatherDomainEntityModel) } }
    }

    @Test
    fun when_get_saved_weather_called_do_not_call_api_method_from_repository() =
        runBlockingTest {
            val query = "query"
            viewModel.getSavedWeather()

            verify(inverse = true) { runBlockingTest { repository.getCurrentWeather(query) } }
        }

    @Test
    fun when_get_saved_weather_called_do_not_call_delete_method_from_repository() =
        runBlockingTest {
            viewModel.getSavedWeather()

            verify(inverse = true) { runBlockingTest { repository.deleteAllWeather() } }
        }
}