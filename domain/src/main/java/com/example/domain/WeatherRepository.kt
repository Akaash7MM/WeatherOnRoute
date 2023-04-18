package com.example.domain

import com.example.domain.models.weather.Timelines
import com.example.domain.models.weather.WeatherTimeline
import com.example.domain.util.Resource

interface WeatherRepository {
    suspend fun getWeatherTimeline(location: String, timeStamp: String): Resource<WeatherTimeline>
}
