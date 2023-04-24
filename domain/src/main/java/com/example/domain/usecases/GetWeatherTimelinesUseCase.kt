package com.example.domain.usecases

import com.example.domain.PointsTime
import com.example.domain.WeatherRepository
import com.example.domain.models.weather.Hourly
import com.example.domain.util.Resource
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

class GetWeatherTimelinesUseCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(pointsTime: List<PointsTime>, timeStep: String) {
        val steppedList = mutableListOf<PointsTime>()
        for (point in 0..pointsTime.size step pointsTime.size / 10 ) {
            steppedList.add(pointsTime[point].copy(timeDateTime =LocalDateTime.now().plusSeconds(pointsTime[point].timeFromOrigin.toLong())))
        }
        val finalPoints = mutableListOf<PointsTime>()
        for (point in steppedList){
            val locationSting =  point.endPoints.first.toString() + ", " +  point.endPoints.second.toString()
            val response = weatherRepository.getWeatherTimeline(locationSting, timeStep)
            when (response) {
                is Resource.Success -> {
                    val weatherList = response.data.timelines.hourly
                    val matchedWeatherHour = weatherList.minByOrNull {
                            val givenTime = LocalDateTime.parse(it.time.dropLast(1))
                            abs(Duration.between(point.timeDateTime,givenTime).toMinutes())
                    }
                    finalPoints.add(point.copy(matchedHourly = matchedWeatherHour))
                }
                is Resource.Failure -> {
                    response.throwable
                }
            }
        }
        for(point1 in finalPoints){
            println(" Here They Are ${point1.timeDateTime.toString()} ${point1.matchedHourly?.time}")
        }



    }
}
