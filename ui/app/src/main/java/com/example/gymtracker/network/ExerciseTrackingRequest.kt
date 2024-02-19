package com.example.gymtracker.network

import java.time.LocalDate
import java.util.UUID

data class ExerciseTrackingRequest(
    val userId: UUID,
    val exerciseId: UUID,
    val performanceMetric: Double,
    val createdDate: String
)