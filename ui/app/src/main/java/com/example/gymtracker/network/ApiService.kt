package com.example.gymtracker.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("Exercise")
    fun getExercises(): Call<List<Exercise>>

    @GET("User")
    fun getUsers(): Call<List<User>>

    @GET("/ExerciseTracking")
    fun getExerciseTracking(): Call<List<ExerciseTracking>>
}