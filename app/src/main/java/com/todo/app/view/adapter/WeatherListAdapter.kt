package com.todo.app.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todo.app.R
import com.todo.app.data.remote.DailyDomainModel
import com.todo.app.databinding.ItemListBinding
import com.todo.app.utils.getDate
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class WeatherListAdapter @Inject constructor(private val listener: OnItemClickListener) :
    ListAdapter<DailyDomainModel, WeatherListAdapter.WeatherListViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherListViewHolder(binding)
    }

    override fun onBindViewHolder(holderWeather: WeatherListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holderWeather.bind(currentItem)
    }

    inner class WeatherListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position))
                    }
                }
            }
        }

        fun bind(domainModel: DailyDomainModel) {
            binding.apply {
                val context = itemView.context
                val weather = domainModel.weather.first()
                day = getDate(domainModel.dt)
                icon = weather.icon
                temp = context.getString(
                    R.string.min_max_temperature,
                    domainModel.temp.max.toString(),
                    domainModel.temp.min.toString()
                )
                desc = weather.description
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(dailyDomainModel: DailyDomainModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<DailyDomainModel>() {
        override fun areItemsTheSame(
            oldItem: DailyDomainModel,
            newItem: DailyDomainModel
        ): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: DailyDomainModel,
            newItem: DailyDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}