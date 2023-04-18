package com.example.weatheronrouteapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatheronrouteapp.ui.MainScreen.LocationInputBox
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MapViewModel) {
    val mapState by viewModel.mapState.collectAsState()
    val uiState by viewModel.locationFields.collectAsState()
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
                        for (point in 0..polyline.size step polyline.size / 10) {
                            Marker(MarkerState(polyline[point]))
                        }
                    }
                }
            }
            LocationInputBox(uiState.originString, uiState.destinationString, viewModel)
            Button(
                onClick = { viewModel.getDirections() },
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight(Alignment.Bottom)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(18.dp)
                    ),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Text(
                    text = "Show Timeline",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
