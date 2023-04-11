package com.example.weatheronroute.model

import kotlinx.serialization.Serializable

@Serializable
data class StartLocation(
    val lat: Double,
    val lng: Double
)