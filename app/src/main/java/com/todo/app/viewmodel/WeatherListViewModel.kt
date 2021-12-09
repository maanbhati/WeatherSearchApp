package com.todo.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.app.data.helper.ModelConverter
import com.todo.app.data.local.WeatherEntityModel
import com.todo.app.data.remote.Resource
import com.todo.app.data.remote.WeatherDomainModel
import com.todo.app.repository.WeatherRepository
import com.todo.networkmodule.dto.CurrentWeatherResponse
import com.todo.networkmodule.dto.FutureWeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val modelConverter: ModelConverter
) : ViewModel() {
    private val _itemResponseCurrent = MutableLiveData<Resource<WeatherDomainModel>>()
    val itemResponseCurrent: MutableLiveData<Resource<WeatherDomainModel>>
        get() = _itemResponseCurrent

    fun searchWeather(query: String) = viewModelScope.launch(Dispatchers.IO) {
        safeWeatherCall(query)
    }

    private suspend fun safeWeatherCall(query: String) {
        _itemResponseCurrent.postValue(Resource.Loading())
        try {
            val currentWeatherResponse = weatherRepository.getCurrentWeather(query)
            if (currentWeatherResponse.isSuccessful) {
                currentWeatherResponse.body()?.let { currentWeather ->
                    val futureWeatherResponse = weatherRepository.getFutureWeather(
                        currentWeather.coord.lat.toString(),
                        currentWeather.coord.lon.toString()
                    )
                    _itemResponseCurrent.postValue(
                        handleWeatherResponse(
                            currentWeather,
                            futureWeatherResponse
                        )
                    )
                }
            } else throw IOException()
        } catch (error: Exception) {
            when (error) {
                is IOException -> _itemResponseCurrent.postValue(Resource.Error("Network failure"))
                else -> _itemResponseCurrent.postValue(Resource.Error("Error in data conversion"))
            }
        }
    }

    private fun handleWeatherResponse(
        currentWeather: CurrentWeatherResponse,
        futureWeatherResponse: Response<FutureWeatherResponse>
    ): Resource<WeatherDomainModel> {
        if (futureWeatherResponse.isSuccessful) {
            futureWeatherResponse.body()?.let { futureWeather ->
                val weatherEntityModel =
                    modelConverter.convertFromDomainToEntityModel(currentWeather, futureWeather)
                updateWeatherData(weatherEntityModel)
                return Resource.Success(
                    modelConverter.convertEntityToDomainModel(weatherEntityModel)
                )
            }
        }
        return Resource.Error(futureWeatherResponse.message())
    }

    fun updateWeatherData(weatherEntityModel: WeatherEntityModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.deleteAllWeather()
                weatherRepository.insertWeather(weatherEntityModel)
            } catch (error: Exception) {
                _itemResponseCurrent.postValue(Resource.Error("Error in data saving"))
            }
        }
    }

    fun getSavedWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherEntityModel = weatherRepository.getSavedWeather()
            val weatherDomainModel =
                modelConverter.convertEntityToDomainModel(weatherEntityModel)
            _itemResponseCurrent.postValue(Resource.Success(weatherDomainModel))
        }
    }
}