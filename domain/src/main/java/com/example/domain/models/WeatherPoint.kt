package com.example.domain.models

import com.example.domain.models.weather.Hourly
import com.example.domain.models.weather.Location

data class WeatherPoint(
    val weatherData: Hourly,
    val reachingTime:String,
    val location: Location
)
