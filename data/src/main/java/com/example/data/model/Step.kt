package com.example.weatheronroute.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    val distance: Distance,
    val duration: Duration,
    val end_location: EndLocation,
    val html_instructions: String,
    val polyline: Polyline,
    val start_location: StartLocation,
    val travel_mode: String
)