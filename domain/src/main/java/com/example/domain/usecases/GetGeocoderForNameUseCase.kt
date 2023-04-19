package com.example.domain.usecases

import com.example.domain.MapsRepository
import kotlinx.coroutines.flow.Flow

class GetGeocoderForNameUseCase(private val mapsRepository: MapsRepository) {
    suspend operator fun invoke(locationName: String): Flow<List<String>> {
        return mapsRepository.getAddressFromGeocoder(locationName)
    }
}
