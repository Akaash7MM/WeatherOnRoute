package com.example.domain.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Values(
    val cloudBase: Double,
    val cloudCeiling: Double,
    val cloudCover: Double,
    val dewPoint: Double,
    val freezingRainIntensity: Int,
    val humidity: Double,
    val precipitationProbability: Int,
    val pressureSurfaceLevel: Double,
    val rainIntensity: Int,
    val sleetIntensity: Int,
    val snowIntensity: Int,
    val temperature: Double,
    val temperatureApparent: Double,
    val uvHealthConcern: Int,
    val uvIndex: Int,
    val visibility: Int,
    val weatherCode: Int,
    val windDirection: Double,
    val windGust: Double,
    val windSpeed: Double
)