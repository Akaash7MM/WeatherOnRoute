package com.example.domain.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double,
    val lon: Double,
    val name: String,
    val type: String
)