package com.example.weatheronroute.model

import kotlinx.serialization.Serializable

@Serializable
data class OverviewPolyline(
    val points: String
)