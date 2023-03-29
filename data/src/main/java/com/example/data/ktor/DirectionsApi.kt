package com.example.weatheronroute.ktor

import com.example.weatheronroute.model.DirectionsResponse

interface DirectionsApi {
    suspend fun getDirections(origin: String, destination: String): DirectionsResponse
}
