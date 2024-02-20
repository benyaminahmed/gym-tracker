package com.example.gymtracker.network.dto

import java.time.LocalDateTime
import java.util.UUID

data class ExerciseTracking(
    val userId: UUID,
    val exerciseId: UUID,
    val firstName: String,
    val lastName: String,
    val exerciseName: String,
    val performanceMetric: Double,
    val unit: String,
    val createdDate: LocalDateTime
)
