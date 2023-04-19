package com.example.weatheronrouteapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatheronrouteapp.ui.MainScreen.LocationInputBox


@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: MapViewModel
) {
    NavHost(
            navController = navController,
            startDestination = "home_screen"
        ) {
        composable("home_screen") {
            MainScreen(navController = navController, viewModel = viewModel)
        }
        composable("location_input") {
                LocationInputBox(navController = navController,viewModel = viewModel)
            }
        }
}
