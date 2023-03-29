package com.example.weatheronroute.ktor

import com.example.weatheronroute.model.DirectionsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.collections.get

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

    override suspend fun getDirections(origin: String, destination: String): DirectionsResponse {
        return client.get("https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&key=AIzaSyB3h_km8dGJ_MP6Q6xDcwQNBrYuwknbn8g") {
        }.body()
    }
}
