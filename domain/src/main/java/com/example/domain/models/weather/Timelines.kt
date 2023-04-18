package com.example.domain.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Timelines(
    val minutely: List<Minutely>
)