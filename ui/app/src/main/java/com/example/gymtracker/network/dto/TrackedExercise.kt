package com.example.gymtracker.network.dto

import java.util.UUID

data class TrackedExercise(
    val exerciseId: UUID,
    val exerciseName: String
)
