package com.example.domain.models

import com.example.domain.models.PointsTime

data class PolylineData(
    val polyline: List<Pair<Double,Double>>,
    val pointsTimeList: List<PointsTime>,
    val polylineDistance:String,
    val polylineDuration:String
)
