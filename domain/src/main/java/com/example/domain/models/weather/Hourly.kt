package com.example.domain.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    val time: String,
    val values: Values
)