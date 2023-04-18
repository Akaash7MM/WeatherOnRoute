package com.example.weatheronroute.ktor

import com.example.data.util.Constants.API_KEY_MAPS
import com.example.data.util.Constants.API_KEY_WEATHER
import com.example.data.util.Constants.GOOGLE_MAPS_BASE_URL
import com.example.data.util.Constants.TOMORROW_WEATHER_BASE_URL
import com.example.domain.DirectionsApi
import com.example.domain.models.maps.DirectionsResponse
import com.example.domain.models.weather.WeatherTimeline
import com.example.domain.util.Resource
import com.example.domain.util.safeResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class DirectionsApiImpl(val engine: HttpClientEngine) : DirectionsApi {
    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
    override suspend fun getDirections(origin: String, destination: String): Resource<DirectionsResponse> {
        return safeResult {
            client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = GOOGLE_MAPS_BASE_URL
                    path("maps", "api", "directions", "json")
                    parameters.append("origin", origin)
                    parameters.append("destination", destination)
                    parameters.append("key", API_KEY_MAPS)
                }
            }.body()
        }
    }

    override suspend fun getWeather(location: String, timeSteps: String): Resource<WeatherTimeline> {
        return safeResult {
            client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = TOMORROW_WEATHER_BASE_URL
                    path("v4", "weather", "forecast")
                    parameters.append("location", location)
                    parameters.append("timesteps", "1m")
                    parameters.append("apikey", API_KEY_WEATHER)
                }
            }.body()
        }
    }
}
