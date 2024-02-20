package com.example.gymtracker.network.dto

data class UserWithPerformance(
    val user: User,
    val maxPerformance: ExerciseTracking?
)