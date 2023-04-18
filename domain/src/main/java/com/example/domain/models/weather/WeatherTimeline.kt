package com.example.domain.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherTimeline(
    val location: Location,
    val timelines: Timelines
)