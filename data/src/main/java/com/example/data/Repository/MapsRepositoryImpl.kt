package com.example.weatheronroute.Repository

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.MapsApi
import com.example.domain.MapsRepository
import com.example.domain.models.maps.DirectionsResponse
import com.example.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class MapsRepositoryImpl(val api: MapsApi, val applicationContext: Context) : MapsRepository {
    override suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse> {
        return withContext(Dispatchers.IO) {
            api.getDirections(origin, destination)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun getAddressFromGeocoder(location: String): Flow<List<String>> {
        val geocoder = Geocoder(applicationContext)
        return withContext(Dispatchers.IO){
            callbackFlow {
                geocoder.getFromLocationName(location, 10) { address ->
                    trySend(
                        address.map {
                            "${it.getAddressLine(0)}, ${it.locality}, ${it.countryName}"
                        }
                    )
                }
                awaitClose()
            }
        }
    }
}
