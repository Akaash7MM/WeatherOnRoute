package com.example.domain.models.maps

import kotlinx.serialization.Serializable

@Serializable
data class DirectionsResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)
