package com.example.domain.models.maps

import kotlinx.serialization.Serializable

@Serializable
data class StartLocation(
    val lat: Double,
    val lng: Double
)