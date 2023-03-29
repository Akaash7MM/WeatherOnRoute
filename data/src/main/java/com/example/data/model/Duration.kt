package com.example.weatheronroute.model

import kotlinx.serialization.Serializable

@Serializable
data class Duration(
    val text: String,
    val value: Int
)