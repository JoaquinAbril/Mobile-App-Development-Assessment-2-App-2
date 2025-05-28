package com.example.weather

data class WeatherX(
    val main: MainWeather,
    val weather: List<WeatherInfo>,
    val wind: Wind,
    val sys: Sys
)

data class MainWeather(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val temp_max: Double,
    val temp_min: Double
)

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
