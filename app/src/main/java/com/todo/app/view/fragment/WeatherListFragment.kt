package com.todo.app.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.todo.app.R
import com.todo.app.data.remote.DailyDomainModel
import com.todo.app.data.remote.Resource
import com.todo.app.data.remote.WeatherDomainModel
import com.todo.app.databinding.FragmentWeatherListBinding
import com.todo.app.utils.getDate
import com.todo.app.utils.hasInternetConnection
import com.todo.app.utils.loadImage
import com.todo.app.view.adapter.WeatherListAdapter
import com.todo.app.viewmodel.WeatherListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherListFragment : Fragment(), WeatherListAdapter.OnItemClickListener {

    private var binding: FragmentWeatherListBinding by viewLifecycle()
    private lateinit var viewModelWeather: WeatherListViewModel
    private lateinit var weatherListAdapter: WeatherListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelWeather = ViewModelProvider(this)[WeatherListViewModel::class.java]
        weatherListAdapter = WeatherListAdapter(this)

        binding.apply {
            rvListItems.setHasFixedSize(true)
            rvListItems.adapter = weatherListAdapter
        }

        val hasInternetConnection = hasInternetConnection(requireContext())
        observeOfflineData(hasInternetConnection)

        var job: Job? = null
        binding.editSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    val query = editable.toString()
                    if (query.isNotEmpty()) {
                        observeData(query, hasInternetConnection)
                    }
                }
            }
        }
    }

    private fun observeData(query: String, hasInternetConnection: Boolean) {
        if (hasInternetConnection) {
            viewModelWeather.searchWeather(query)
            observeItemResponse()
        }
    }

    private fun observeOfflineData(hasInternetConnection: Boolean) {
        if (!hasInternetConnection) {
            viewModelWeather.getSavedWeather()
            observeItemResponse()
        }
    }

    private fun observeItemResponse() {
        viewModelWeather.itemResponseCurrent.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    shouldShowProgressBar(binding.progressBar, false)
                    it.data?.let { itemResponse ->
                        updateUi(itemResponse)
                    }
                }
                is Resource.Error -> {
                    shouldShowProgressBar(binding.progressBar, false)
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e("ListFragment", message)
                    }
                }
                is Resource.Loading -> {
                    shouldShowProgressBar(binding.progressBar, true)
                }
            }
        }
    }

    private fun updateUi(weatherDomainModel: WeatherDomainModel) {
        binding.apply {
            cityName.text = weatherDomainModel.name
            currentTemperature.text = requireContext().getString(
                R.string.day_temperature,
                weatherDomainModel.temp.toString()
            )
            weatherIcon.loadImage(weatherDomainModel.icon)
            currentWeather.text = weatherDomainModel.description
            currentDate.text = getDate(weatherDomainModel.dt)
            weatherListAdapter.submitList(weatherDomainModel.daily)
        }
    }

    private fun shouldShowProgressBar(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onItemClick(domainModel: DailyDomainModel) {
        binding.apply {
            val weather = domainModel.weather.first()
            currentTemperature.text = requireContext().getString(
                R.string.day_temperature,
                domainModel.temp.day.toString()
            )
            weatherIcon.loadImage(weather.icon)
            currentWeather.text = weather.description
            currentDate.text = getDate(domainModel.dt)
        }
    }
}