package com.todo.app.data.remote.domainmodel

import com.todo.app.data.remote.DailyDomainModel
import com.todo.app.data.remote.WeatherDomainModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherDomainModelTest {
    @MockK
    private lateinit var dailyDomainModel: DailyDomainModel

    private lateinit var domainModel: WeatherDomainModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        domainModel = WeatherDomainModel(
            123,
            1236878,
            "CityName",
            15.22,
            10.44,
            20.67,
            "Cloudy",
            "Icon",
            listOf(dailyDomainModel)
        )
    }

    @Test
    fun verify_domain_model_data() {
        assert(domainModel.id == 123)
        assert(domainModel.name == "CityName")
        assert(domainModel.temp == 15.22)
        assert(domainModel.temp_min == 10.44)
        assert(domainModel.temp_max == 20.67)
        assert(domainModel.description == "Cloudy")
        assert(domainModel.icon == "Icon")
        assert(domainModel.daily == listOf(dailyDomainModel))
    }
}