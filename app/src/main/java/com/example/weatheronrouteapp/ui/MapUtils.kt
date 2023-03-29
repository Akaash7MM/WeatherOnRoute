package com.example.weatheronroute.android

import com.example.weatheronroute.model.LatLang
import com.google.android.gms.maps.model.LatLng

object MapUtils {
    fun LatLang.toRealLatLng(): LatLng {
        return LatLng(this.latitude, this.longitude)
    }
}
