package com.example.data.Repository

import com.example.domain.DirectionsApi
import com.example.domain.WeatherRepository
import com.example.domain.models.weather.Timelines
import com.example.domain.models.weather.WeatherTimeline
import com.example.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(val api: DirectionsApi) : WeatherRepository {

    override suspend fun getWeatherTimeline(
        location: String,
        timeStamp: String
    ): Resource<WeatherTimeline> {
        return withContext(Dispatchers.IO) {
            api.getWeather(location, timeStamp)
        }
    }
}
