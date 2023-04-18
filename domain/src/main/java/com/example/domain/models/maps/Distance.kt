package com.example.domain.models.maps

import kotlinx.serialization.Serializable

@Serializable
data class Distance(
    val text: String,
    val value: Int
)