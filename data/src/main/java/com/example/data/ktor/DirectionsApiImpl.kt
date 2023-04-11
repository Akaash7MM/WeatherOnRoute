package com.example.weatheronroute.ktor

import com.example.data.util.Constants.API_KEY
import com.example.data.util.Constants.GOOGLE_MAPS_BASE_URL
import com.example.domain.DirectionsApi
import com.example.domain.util.Resource
import com.example.domain.util.safeResult
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
                    parameters.append("key", API_KEY)
                }
            }.body()
        }
    }
}
