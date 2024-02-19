package com.example.gymtracker.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("Exercise")
    fun getExercises(): Call<List<Exercise>>

    @GET("User")
    fun getUsers(): Call<List<User>>

    @GET("/ExerciseTracking")
    fun getExerciseTracking(): Call<List<ExerciseTracking>>
    @POST("/ExerciseTracking")
    suspend fun postExerciseTracking(@Body exerciseTracking: ExerciseTrackingRequest): Response<Void>

}