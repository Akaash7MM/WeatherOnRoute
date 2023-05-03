package com.example.weatheronrouteapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.data.Repository.WeatherRepositoryImpl
import com.example.domain.MapsApi
import com.example.domain.MapsRepository
import com.example.domain.WeatherRepository
import com.example.domain.usecases.GetGeocoderForNameUseCase
import com.example.domain.usecases.GetPolylineForNamesUsecase
import com.example.domain.usecases.GetWeatherTimelinesUseCase
import com.example.weatheronroute.Repository.MapsRepositoryImpl
import com.example.weatheronroute.ktor.MapsApiImpl
import com.example.weatheronrouteapp.theme.WeatherOnRouteAppTheme
import com.example.weatheronrouteapp.ui.MapViewModel
import com.example.weatheronrouteapp.ui.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.elevation.SurfaceColors
import io.ktor.client.engine.android.*

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val api: MapsApi = MapsApiImpl(AndroidClientEngine(config = AndroidEngineConfig()))
    private val mapRepo: MapsRepository = MapsRepositoryImpl(api,this)
    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl(api)
    private val usecaseMap = GetPolylineForNamesUsecase(mapRepo)
    private val usecaseAddress = GetGeocoderForNameUseCase(mapRepo)
    private val usecaseWeather = GetWeatherTimelinesUseCase(weatherRepository)
    private val viewModel: MapViewModel by lazy {
        MapViewModel(usecaseMap, usecaseWeather,usecaseAddress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = SurfaceColors.SURFACE_5.getColor(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        setContent {
            WeatherOnRouteAppTheme {
                Navigation(viewModel = viewModel)
            }
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.getDeviceLocation(fusedLocationProviderClient)
            }
        }

    private fun askPermissions() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) -> {
            viewModel.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
