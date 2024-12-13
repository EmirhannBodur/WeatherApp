package com.emirhan.apideneme.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emirhan.apideneme.R
import com.emirhan.apideneme.adapter.forecastAdapter
import com.emirhan.apideneme.viewModel.WeatherViewModel
import com.emirhan.apideneme.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    @SuppressLint("NotifyDataSetChanged", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(lightScrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT)
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Arka planı saat dilimine göre değiştirmek için container değişkeni
        val container:LinearLayout=binding.container


        // Şu anki zamanı alıyoruz
        val currenTime=LocalTime.now()


        // Saat dilimine göre arka plan resmi seçiyoruz
        if (currenTime.isAfter(LocalTime.of(6,0))&&currenTime.isBefore(LocalTime.of(12,0))){
            container.setBackgroundResource(R.drawable.morning)
        }else if(currenTime.isAfter(LocalTime.of(18,0))&&currenTime.isBefore(LocalTime.of(21,0))){
            container.setBackgroundResource(R.drawable.evening)
        }else{
            container.setBackgroundResource(R.drawable.night)
        }


        // Unix zamanını tarih ve saat formatına dönüştüren fonksiyon
        fun convertUnixToDateTime(unixTime: Long): String
        { val date = Date(unixTime * 1000)
            val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            return format.format(date) }


        //Recyleview ayarı
        val adapterr=forecastAdapter(mutableListOf())
        binding.forecastRec.apply {
            layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter=adapterr
        }
        // Hava durumu verisini gözlemleyip UI'yi güncelliyoruz
        viewModel.weather.observe(this, Observer { weather->
            weather?.let {
                binding.city.text=it.name
                binding.temperature.text="${it.main?.temp} ℃ "
                binding.aciklama.text= it.weather!![0].description
                binding.humidityTxt.text= "% ${it.main?.humidity}"
                binding.windTxt.text="${it.wind?.speed} km/s"

                val iconUrl="https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png"
                Glide.with(this)
                    .load(iconUrl)
                    .into(binding.icon)
                binding.minTempView.text="${it.main?.tempMin} ℃"
                binding.maxTempView.text="${it.main?.tempMax} ℃"
                binding.SunRiseView.text=it.sys?.sunrise?.let {
                    unixTime -> convertUnixToDateTime(unixTime)
                }
                binding.SunSetView.text=it.sys?.sunset?.let {
                    unixTime -> convertUnixToDateTime(unixTime)

                }
                binding.feelsTempView.text="${it.main?.feelsLike} ℃"
            }
        })

        // Hava durumu verisini gözlemleyip UI'yi güncelliyoruz
        viewModel.forecastWeather.observe(this, Observer {
            forecast->
            forecast?.let {
                adapterr.updateForecast(it.list) // Adapteri güncelliyoruz
            }
        })

        // Kullanıcı şehir adı girdiğinde hava durumu verilerini çekiyoruz
        binding.addCityBtn.setOnClickListener {
            val cityName=binding.addCity.text.toString()
            viewModel.fetchWeather(cityName)
            viewModel.fetchForeCast(cityName)
        }

        //Varsayılan şehir
        viewModel.fetchWeather("İstanbul")
        viewModel.fetchForeCast("İstanbul")
    }
}