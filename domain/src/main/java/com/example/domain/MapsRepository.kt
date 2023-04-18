package com.example.domain

import com.example.domain.models.maps.DirectionsResponse
import com.example.domain.util.Resource

interface MapsRepository {
    suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse>
}
