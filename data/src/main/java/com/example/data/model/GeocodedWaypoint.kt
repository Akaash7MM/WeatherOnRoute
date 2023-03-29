package com.example.weatheronroute.model

import kotlinx.serialization.Serializable


@Serializable
data class GeocodedWaypoint(
    val geocoder_status: String,
    val partial_match: Boolean = true,
    val place_id: String,
    val types: List<String>
)