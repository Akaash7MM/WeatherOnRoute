package com.example.weatheronroute.model

import kotlinx.serialization.Serializable

@Serializable
data class Polyline(
    val points: String
)