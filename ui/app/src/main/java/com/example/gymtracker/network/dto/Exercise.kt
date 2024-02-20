package com.example.gymtracker.network.dto

import java.util.UUID

data class Exercise(
    val exerciseId: UUID,
    val exerciseName: String,
    val unit: String,
    val createdDate: String
)