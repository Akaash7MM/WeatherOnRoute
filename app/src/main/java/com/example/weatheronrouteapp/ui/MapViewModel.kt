package com.example.weatheronrouteapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetPolylineForNamesUsecase
import com.example.domain.util.Resource
import com.example.weatheronroute.android.LocationFields
import com.example.weatheronroute.android.MapState
import com.example.weatheronrouteapp.util.Mapper.toLatLng
import com.example.weatheronrouteapp.util.SnackbarEvents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MapViewModel(val polylineForNamesUsecase: GetPolylineForNamesUsecase) : ViewModel() {

    val mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    private val _locationFields: MutableStateFlow<LocationFields> = MutableStateFlow(LocationFields())
    val locationFields = _locationFields

    private val errorEvent: Channel<SnackbarEvents> = Channel()
    val _errorEvent = errorEvent.receiveAsFlow()

    init {
        setUiStateData(_locationFields.value.originString, _locationFields.value.destinationString)
    }
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

    fun getDirections() {
        viewModelScope.launch {
            val response = polylineForNamesUsecase(_locationFields.value.originString, _locationFields.value.destinationString)
            when (response) {
                is Resource.Success -> {
                    val latlngList = response.data.map { it.toLatLng() }
                    val startPointState = CameraPositionState(CameraPosition(latlngList[0], 7.0F, 0.0F, 0.0F))
                    mapState.tryEmit(MapState(cameraLocationZoom = startPointState, polylines = latlngList))
                }
                is Resource.Failure -> {
                    errorEvent.send(SnackbarEvents(response.throwable.message.toString()))
                }
            }
        }
    }
    fun setUiStateData(origin: String, destination: String) {
        _locationFields.value = _locationFields.value.copy(originString = origin, destinationString = destination)
    }

    fun setCameraLocationZoom(latLng: LatLng, zoom: Float) {
        mapState.tryEmit(mapState.value.copy(cameraLocationZoom = CameraPositionState(CameraPosition(latLng, zoom, 0.0F, 0.0F))))
    }
}
