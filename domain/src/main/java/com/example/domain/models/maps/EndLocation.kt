package com.example.domain.models.maps

import kotlinx.serialization.Serializable

@Serializable
data class EndLocation(
    val lat: Double,
    val lng: Double
)