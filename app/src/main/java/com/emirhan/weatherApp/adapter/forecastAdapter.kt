package com.emirhan.weatherApp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirhan.apideneme.databinding.RecRowBinding
import com.emirhan.weatherApp.model.WeatherInfo

class forecastAdapter(private val forecastList:MutableList<WeatherInfo>):RecyclerView.Adapter<forecastAdapter.forecastViewHolder>() {


    class forecastViewHolder(val bag: RecRowBinding):RecyclerView.ViewHolder(bag.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): forecastViewHolder {
        val binding=RecRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return forecastViewHolder(binding)
    }

    override fun getItemCount(): Int =forecastList.size

    override fun onBindViewHolder(holder: forecastViewHolder, position: Int) {
        val item=forecastList[position]
        val binding=holder.bag

        binding.forecastTextView1.text=item.dt_txt
        binding.forecastTemparature.text="${item.main.temp} ℃"
        val iconUrl="https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        Glide.with(binding.forecastIcon.context)
            .load(iconUrl)
            .into(binding.forecastIcon)
    }
    fun updateForecast(newList: List<WeatherInfo>) {
        forecastList.clear()
        forecastList.addAll(newList)
        notifyDataSetChanged()
    }
}