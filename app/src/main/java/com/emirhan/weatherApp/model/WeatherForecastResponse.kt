package com.emirhan.weatherApp.model

data class WeatherForecastResponse(
    val list: List<WeatherInfo>, // Tahmin edilen günlerin listesi
    val city: CityInfo          // Şehir bilgileri
)
data class WeatherInfo(
    val dt: Long,               // Unix zaman damgası
    val main: MainInfo,         // Sıcaklık, nem gibi ana bilgiler
    val weather: List<WeatherDescription>, // Hava açıklaması (bir liste)
    val wind: WindInfo,         // Rüzgar bilgileri
    val dt_txt: String          // Tarih ve saat (metin formatında)
)
data class MainInfo(
    val temp: Double,           // Anlık sıcaklık
    val feels_like: Double,     // Hissedilen sıcaklık
    val temp_min: Double,       // Minimum sıcaklık
    val temp_max: Double,       // Maksimum sıcaklık
    val humidity: Int           // Nem oranı (%)
)
data class WeatherDescription(
    val description: String,    // Hava durumu açıklaması
    val icon: String            // İkon kodu
)
data class WindInfo(
    val speed: Double           // Rüzgar hızı (m/s)
)
data class CityInfo(
    val name: String,           // Şehir adı
    val country: String         // Ülke kodu
)
