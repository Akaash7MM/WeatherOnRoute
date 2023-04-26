package com.example.domain.usecases

import com.example.domain.MapsRepository
import com.example.domain.models.PointsTime
import com.example.domain.models.PolylineData
import com.example.domain.util.Resource

class GetPolylineForNamesUsecase(private val mapsRepository: MapsRepository) {
    suspend operator fun invoke(origin: String, destination: String): Resource<PolylineData> {
        val response = mapsRepository.getDirections(origin, destination)
        return when (response) {
            is Resource.Success -> {
                val latlngList = mutableListOf<Pair<Double, Double>>()
                val pointsTime = mutableListOf<PointsTime>()
                val steps = response.data.routes[0].legs[0].steps
                for (step in steps) {
                    val timeFromOrigin = if (pointsTime.lastIndex == -1)step.duration.value else pointsTime[pointsTime.lastIndex].timeFromOrigin + step.duration.value
                    val endLocation = step.end_location
                    val endLocationPair = Pair(endLocation.lat, endLocation.lng)
                    pointsTime.add(PointsTime(endLocationPair, timeFromOrigin))
                    latlngList.addAll(decodePoly(step.polyline.points))
                }
                val polylineData = PolylineData(latlngList, pointsTime)
                Resource.Success(polylineData)
            }
            is Resource.Failure -> response
        }
    }

    private fun decodePoly(encoded: String): List<Pair<Double, Double>> {
        val poly = mutableListOf<Pair<Double, Double>>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = Pair(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }
}
