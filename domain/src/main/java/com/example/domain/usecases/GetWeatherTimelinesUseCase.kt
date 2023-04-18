package com.example.domain.usecases

import com.example.domain.PointsTime
import com.example.domain.WeatherRepository
import com.example.domain.util.Resource
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class GetWeatherTimelinesUseCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(pointsTime: List<PointsTime>, timeStep: String) {
        val pointTime = pointsTime[0]
        val locationSting = pointTime.endPoints.first.toString() + ", " + pointTime.endPoints.second.toString()
        val response = weatherRepository.getWeatherTimeline(locationSting, timeStep)
        when (response) {
            is Resource.Success -> {
                val currentTime = LocalDateTime.now()
                for (minute in response.data.timelines.minutely) {
                    currentTime.plusSeconds(pointTime.timeFromOrigin.toLong())
                    val givenTime = LocalDateTime.parse(minute.time.dropLast(1))
//                    val timeDiffSeconds = ChronoUnit.SECONDS.between(currentTime, givenTime).coerceAtLeast(0)
                    println("time string ${pointTime.timeFromOrigin}  $givenTime")
                }
            }
            is Resource.Failure -> {
                response.throwable
            }
        }
    }
}
