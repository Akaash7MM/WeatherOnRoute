package com.example.domain

import java.awt.geom.Point2D

data class PolylineData(
    val polyline: List<Pair<Double,Double>>,
    val pointsTimeList: List<PointsTime>
)
