package com.example.gymtracker.network.dto

data class UserWithPerformance(
    val user: User,
    val maxPerformance: ExerciseTracking? // Can be null if there's no performance data
)