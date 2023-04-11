package com.example.domain

interface WeatherApi {
    suspend fun getWeatherForLocation(location: String)
}
