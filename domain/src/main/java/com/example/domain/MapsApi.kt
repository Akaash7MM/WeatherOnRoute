package com.example.domain

import com.example.domain.util.Resource
import com.example.domain.models.maps.DirectionsResponse
import com.example.domain.models.weather.WeatherTimeline


interface MapsApi {
    suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse>
    suspend fun getWeather(location: String, timeSteps: String): Resource<WeatherTimeline>

}