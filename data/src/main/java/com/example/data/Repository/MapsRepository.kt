package com.example.weatheronroute.Repository

import com.example.weatheronroute.model.DirectionsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface MapsRepository {
    val couroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    suspend fun getDirections(origin: String, destination: String): DirectionsResponse
}
