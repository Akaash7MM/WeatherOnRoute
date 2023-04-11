package com.example.weatheronrouteapp.util

import com.google.android.gms.maps.model.LatLng

object Mapper {
    fun Pair<Double, Double>.toLatLng(): LatLng {
        return LatLng(this.first, this.second)
    }
}
