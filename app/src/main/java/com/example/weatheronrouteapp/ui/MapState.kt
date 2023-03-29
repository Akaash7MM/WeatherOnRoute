package com.example.weatheronroute.android

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

data class MapState(
    val lastKnownLocation: Location?,
    val polylines: List<LatLng>?,
    val cameraLocationZoom: CameraPositionState?
)
