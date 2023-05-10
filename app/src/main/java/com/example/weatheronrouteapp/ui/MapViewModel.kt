package com.example.weatheronrouteapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.PointsTime
import com.example.domain.models.WeatherPoint
import com.example.domain.usecases.GetGeocoderForNameUseCase
import com.example.domain.usecases.GetPolylineForNamesUsecase
import com.example.domain.usecases.GetWeatherTimelinesUseCase
import com.example.domain.util.Resource
import com.example.weatheronroute.android.LocationFields
import com.example.weatheronroute.android.MapState
import com.example.weatheronrouteapp.util.Mapper.toLatLng
import com.example.weatheronrouteapp.util.SnackbarEvents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    val polylineForNamesUsecase: GetPolylineForNamesUsecase,
    val weatherTimelinesUseCase: GetWeatherTimelinesUseCase,
    val getGeocoderForNameUseCase: GetGeocoderForNameUseCase
) : ViewModel() {

    val mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())

    private val _locationFields: MutableStateFlow<LocationFields> = MutableStateFlow(LocationFields())
    val locationFields = _locationFields.asStateFlow()

    private val _hourlyPoints = MutableSharedFlow<List<WeatherPoint>>()
    val hourlyPoints = _hourlyPoints.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<SnackbarEvents>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _addressList: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val addressList = _addressList.asStateFlow()

    private val _lastEditedWasStart: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val lastEditedWasStart = _lastEditedWasStart.asStateFlow()

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
            val polylineResponse = polylineForNamesUsecase(_locationFields.value.originString, _locationFields.value.destinationString)
            when (polylineResponse) {
                is Resource.Success -> {
                    val latlngList = polylineResponse.data.polyline.map { it.toLatLng() }
                    val duration = polylineResponse.data.polylineDuration
                    val distance = polylineResponse.data.polylineDistance

                    val startPointState = CameraPositionState(CameraPosition(latlngList[0], 7.0F, 0.0F, 0.0F))
                    mapState.emit(MapState(
                        cameraLocationZoom = startPointState,
                        polylines = latlngList,
                        duration = duration,
                        distance = distance
                    ))
                    getWeatherTimelines(polylineResponse.data.pointsTimeList)
                }
                is Resource.Failure -> {
                    _errorEvent.emit(SnackbarEvents(polylineResponse.throwable.message.toString()))
                }
                else -> {}
            }
        }
    }

    private fun getWeatherTimelines(pointsTimeList: List<PointsTime>) {
        viewModelScope.launch {
            val weatherResponse = weatherTimelinesUseCase(pointsTimeList, "1h")
            when(weatherResponse){
                is Resource.Success  -> {
                    _hourlyPoints.emit(weatherResponse.data)
                }
                is Resource.Failure -> {
                    _errorEvent.emit(SnackbarEvents(weatherResponse.throwable.message.toString()))

                }
            }
        }
    }

    fun setUiStateData(origin: String, destination: String, lastEdited: Boolean) {
        _locationFields.value = _locationFields.value.copy(originString = origin, destinationString = destination)
        _lastEditedWasStart.value = lastEdited
    }
    fun getGeocoderData(locationName: String) {
        viewModelScope.launch {
            getGeocoderForNameUseCase(locationName).collectLatest { addressList ->
                _addressList.emit(addressList)
            }
        }
    }

    fun resetLocationFields() {
        _locationFields.value = LocationFields("", "")
        _addressList.value = emptyList()
    }

    fun setCameraLocationZoom(latLng: LatLng, zoom: Float) {
        mapState.tryEmit(mapState.value.copy(cameraLocationZoom = CameraPositionState(CameraPosition(latLng, zoom, 0.0F, 0.0F))))
    }
}
