package com.example.weatheronrouteapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheronroute.Repository.MapsRepository
import com.example.weatheronroute.android.MapState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val mapsRepository: MapsRepository) : ViewModel() {

    val mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState(null, null, null))

    fun getDeviceLocation(fusedLocationProviderClient: FusedLocationProviderClient) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mapState.tryEmit(mapState.value.copy(lastKnownLocation = task.result))
                }
            }
        } catch (_: SecurityException) {
        }
    }

    fun getDirections(origin: String, destination: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var latlngList: List<LatLng> = ArrayList()
            val steps = mapsRepository.getDirections(
                origin,
                destination
            ).routes[0].legs[0].steps

            for (step in steps) {
                latlngList = latlngList + decodePoly(step.polyline.points)
            }
            val startPointState = CameraPositionState(CameraPosition(latlngList[0], 7.0F, 0.0F, 0.0F))
            mapState.tryEmit(mapState.value.copy(cameraLocationZoom = startPointState, polylines = latlngList))
        }
    }

    fun setCameraLocationZoom(latLng: LatLng, zoom: Float) {
        mapState.tryEmit(mapState.value.copy(cameraLocationZoom = CameraPositionState(CameraPosition(latLng, zoom, 0.0F, 0.0F))))
    }
    fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }
}
