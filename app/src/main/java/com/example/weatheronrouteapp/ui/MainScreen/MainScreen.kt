package com.example.weatheronrouteapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatheronrouteapp.ui.MainScreen.EnterLocationField
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MapViewModel, navController: NavController) {
    val mapState by viewModel.mapState.collectAsState()
    val weatherPoints by viewModel.hourlyPoints.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    val errorEvents by viewModel.errorEvent.collectAsState(initial = null)
    Scaffold(scaffoldState = scaffoldState) {
        val mapProperties = MapProperties(
            isMyLocationEnabled = mapState.lastKnownLocation != null
        )
        val initialLocation = LatLng(mapState.lastKnownLocation?.latitude ?: 0.0, mapState.lastKnownLocation?.longitude ?: 0.0)
        val cameraPositionState = CameraPositionState(CameraPosition(initialLocation, 0.0F, 0.0F, 0.0F))

        LaunchedEffect(key1 = errorEvents) {
            errorEvents?.message?.let {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GoogleMap(
                properties = mapProperties,
                cameraPositionState = mapState.cameraLocationZoom ?: cameraPositionState
            ) {
                val polyline: List<LatLng>? = mapState.polylines
                polyline?.let {
                    if (polyline.isNotEmpty()) {
                        Polyline(points = polyline, color = Color.Blue)
                        for (point in weatherPoints) {
                            val markerState = MarkerState(
                                position = LatLng(point.location.lat, point.location.lon)
                            )
                            val timeString = point.reachingTime
                            val temprature = point.weatherData.values.temperature

                            MarkerInfoWindowContent(markerState, content = { marker ->
                                Column() {
                                    Text(text = temprature.toString() + "C", style = TextStyle(fontSize = 22.sp))
                                    Text(text = timeString, style = TextStyle(fontSize = 16.sp))
                                }
                            })
                        }
                    }
                }
            }
            EnterLocationField(navController = navController, viewModel = viewModel)
        }
    }
}
