package com.example.gymtracker.network.dto

import java.util.UUID

data class ExerciseWithMuscleGroup(
    val muscleGroup: String,
    val exerciseId: UUID,
    val exerciseName: String,
    val unit: String,
    val createdDate: String
)
