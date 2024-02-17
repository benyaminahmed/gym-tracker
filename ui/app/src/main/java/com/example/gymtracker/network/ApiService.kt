package com.example.gymtracker.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("exercise")
    fun getExercises(): Call<List<Exercise>>
}