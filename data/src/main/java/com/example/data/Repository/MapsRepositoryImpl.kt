package com.example.weatheronroute.Repository

import com.example.weatheronroute.ktor.DirectionsApi
import com.example.weatheronroute.model.DirectionsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MapsRepositoryImpl(val api: DirectionsApi) : MapsRepository {
    override suspend fun getDirections(origin: String, destination: String): DirectionsResponse {
        return withContext(couroutineDispatcher) {
            api.getDirections(origin, destination)
        }
    }
}
