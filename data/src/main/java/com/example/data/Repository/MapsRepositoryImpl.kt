package com.example.weatheronroute.Repository

import com.example.domain.DirectionsApi
import com.example.domain.MapsRepository
import com.example.domain.util.Resource
import com.example.domain.models.maps.DirectionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapsRepositoryImpl(val api: DirectionsApi) : MapsRepository {
    override suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse> {
        return withContext(Dispatchers.IO) {
            api.getDirections(origin, destination)
        }
    }
}
