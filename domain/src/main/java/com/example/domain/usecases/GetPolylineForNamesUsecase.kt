package com.example.domain.usecases

import com.example.domain.MapsRepository
import com.example.domain.util.Resource

class GetPolylineForNamesUsecase(val mapsRepository: MapsRepository) {
    suspend operator fun invoke(origin: String, destination: String): Resource<List<Pair<Double, Double>>> {
        val response = mapsRepository.getDirections(origin, destination)
        return when (response) {
            is Resource.Success -> {
                val latlngList = mutableListOf<Pair<Double, Double>>()
                val steps = response.data.routes[0].legs[0].steps
                for (step in steps) {
                    latlngList.addAll(decodePoly(step.polyline.points))
                }
                Resource.Success(latlngList)
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
