package com.example.domain

import com.example.domain.models.maps.DirectionsResponse
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MapsRepository {
    suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse>
    suspend fun getAddressFromGeocoder(location: String): Flow<List<String>>

}
