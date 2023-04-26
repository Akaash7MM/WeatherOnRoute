package com.example.domain.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Values(
    val cloudBase: Double?,
    val cloudCeiling: Double?,
    val cloudCover: Double?,
    val dewPoint: Double?,
    val evapotranspiration: Double?,
    val freezingRainIntensity: Int?,
    val humidity: Double?,
    val iceAccumulation: Int?,
    val iceAccumulationLwe: Int?,
    val precipitationProbability: Int?,
    val pressureSurfaceLevel: Double?,
    val rainAccumulation: Double?,
    val rainAccumulationLwe: Double?,
    val rainIntensity: Double?,
    val sleetAccumulation: Int?,
    val sleetAccumulationLwe: Int?,
    val sleetIntensity: Int?,
    val snowAccumulation: Int?,
    val snowAccumulationLwe: Int?,
    val snowIntensity: Int?,
    val temperature: Double?,

)