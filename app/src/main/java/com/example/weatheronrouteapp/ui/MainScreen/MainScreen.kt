package com.example.weatheronrouteapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
                val timeString = "11:30a"
                val temperature = "34"
                val markerState = rememberMarkerState(
                    position = LatLng(30.9010, 75.8573)
                )
                MarkerInfoWindow(
                    markerState,
                    icon = null,
                    content = { marker ->
                        Surface(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$temperature\u00B0",
                                    style = TextStyle(fontSize = 18.sp)
                                )
                                Text(text = timeString, style = TextStyle(fontSize = 16.sp))
                            }
                        }
                    }
                )
                markerState.showInfoWindow()

                val polyline: List<LatLng>? = mapState.polylines
                polyline?.let {
                    if (polyline.isNotEmpty()) {
                        Polyline(points = polyline, color = Color.Blue)
                    }
                }
            }
            EnterLocationField(navController = navController, viewModel = viewModel)
        }
    }
}
