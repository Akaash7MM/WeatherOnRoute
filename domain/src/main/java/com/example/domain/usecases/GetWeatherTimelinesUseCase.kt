package com.example.domain.usecases

import com.example.domain.WeatherRepository
import com.example.domain.models.PointsTime
import com.example.domain.models.WeatherPoint
import com.example.domain.models.weather.Hourly
import com.example.domain.util.Resource
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class GetWeatherTimelinesUseCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(pointsTime: List<PointsTime>, timeStep: String): Resource<List<WeatherPoint>> {
        val steppedList = mutableListOf<PointsTime>()
        for (point in 0..pointsTime.size step pointsTime.size / 10) {
            steppedList.add(pointsTime[point].copy(timeDateTime = LocalDateTime.now().plusSeconds(pointsTime[point].timeFromOrigin.toLong())))
        }
        val finalPoints = mutableListOf<WeatherPoint>()
//        for (point in steppedList) {
            // To only get a single marker to prevent multiple calls
            val point = steppedList[0]
            val timeFormatted = point.timeDateTime?.format(DateTimeFormatter.ofPattern("h:mm a a"))?.dropLast(2)
            val locationString =
                point.endPoints.first.toString() + ", " + point.endPoints.second.toString()
            val response = weatherRepository.getWeatherTimeline(locationString, timeStep)
            when (response) {
                is Resource.Success -> {
                    val weatherList = response.data.timelines.hourly
                    val matchedWeatherHour: Hourly? = weatherList.minByOrNull {
                        val givenTime = LocalDateTime.parse(it.time.dropLast(1))
                        abs(Duration.between(point.timeDateTime, givenTime).toMinutes())
                    }
                    matchedWeatherHour?.let {
                        timeFormatted?.let {
                            finalPoints.add(
                                WeatherPoint(
                                    weatherData = matchedWeatherHour,
                                    timeFormatted,
                                    response.data.location
                                )
                            )
                        }
                    }
                    return Resource.Success(finalPoints)
                }
                is Resource.Failure -> {
                    return response
                }
            }
        }
//        return Resource.Success(finalPoints)
    }
