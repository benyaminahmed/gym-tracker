package com.example.gymtracker.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.gymtracker.BuildConfig

object RetrofitService {
    private const val BASE_URL = "https://gymtrackerapi20240217112930.azurewebsites.net/"

    //TODO: Implement a secure method to retrieve and use the API key,
    // avoiding hardcoding it here to enhance security
    private const val API_KEY = BuildConfig.API_KEY

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("X-API-Key", API_KEY)
                .build()
            chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}