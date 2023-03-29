package com.example.weatheronrouteapp.ui

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

@Composable
fun MainScreen(viewModel: MapViewModel) {
    val mapState by viewModel.mapState.collectAsState()
    val mapProperties = MapProperties(
        isMyLocationEnabled = mapState.lastKnownLocation != null
    )
    val initialLocation = LatLng(mapState.lastKnownLocation?.latitude ?: 0.0, mapState.lastKnownLocation?.longitude ?: 0.0)
    val cameraPositionState = CameraPositionState(CameraPosition(initialLocation, 0.0F, 0.0F, 0.0F))
    val startLocation = remember {
        mutableStateOf("")
    }
    val endLocation = remember {
        mutableStateOf("")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        GoogleMap(
            properties = mapProperties,
            cameraPositionState = mapState.cameraLocationZoom ?: cameraPositionState
        ) {
            val polyline = mapState.polylines
            Polyline(points = polyline?: emptyList(), color = Color.Blue)
        }
        LocationInputBox(startLocation, endLocation)
        Button(
            onClick = { viewModel.getDirections(startLocation.value.trim(), endLocation.value.trim()) },
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
