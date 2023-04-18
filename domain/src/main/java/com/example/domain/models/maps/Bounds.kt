package com.example.domain.models.maps

import kotlinx.serialization.Serializable

@Serializable
data class Bounds(
    val northeast: Northeast,
    val southwest: Southwest
)