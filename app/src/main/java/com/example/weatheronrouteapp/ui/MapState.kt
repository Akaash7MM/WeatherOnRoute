package com.example.weatheronroute.android

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

data class MapState(
    val lastKnownLocation: Location? = null,
    val polylines: List<LatLng>? = null,
    val cameraLocationZoom: CameraPositionState? = null,
    val errorSnackbarMessage: String? = null
)
