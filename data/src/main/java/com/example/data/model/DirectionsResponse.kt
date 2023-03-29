package com.example.weatheronroute.model

import kotlinx.serialization.Serializable

@Serializable
data class DirectionsResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)
