package com.example.domain

import com.example.domain.util.Resource
import com.example.weatheronroute.model.DirectionsResponse


interface DirectionsApi {
    suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse>
}
