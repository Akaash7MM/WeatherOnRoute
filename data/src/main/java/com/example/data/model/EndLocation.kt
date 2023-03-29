package com.example.weatheronroute.model

import kotlinx.serialization.Serializable

@Serializable
data class EndLocation(
    val lat: Double,
    val lng: Double
)