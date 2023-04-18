package com.example.domain.models.maps

import kotlinx.serialization.Serializable

@Serializable
data class Duration(
    val text: String,
    val value: Int
)