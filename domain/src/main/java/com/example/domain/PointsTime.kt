package com.example.domain

import com.example.domain.models.weather.Hourly
import java.time.LocalDateTime

data class PointsTime(
    val endPoints: Pair<Double, Double>,
    val timeFromOrigin: Int,
    val timeDateTime : LocalDateTime? = null,
    val matchedHourly: Hourly? = null
)
