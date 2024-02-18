package com.example.gymtracker.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.gymtracker.BuildConfig
import okhttp3.Cache

object RetrofitService {
    private const val BASE_URL = "https://gymtrackerapi20240217112930.azurewebsites.net/"

    //TODO: Implement a secure method to retrieve and use the API key,
    // avoiding hardcoding it here to enhance security
    private const val API_KEY = BuildConfig.API_KEY

    // List of endpoints to exclude from cache
    val excludeFromCache = listOf("/ExerciseTracking", "/ExerciseTracking/MaxPerformance")

    fun create(context: Context): ApiService {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                // Determine if the request should be excluded from cache
                val shouldExcludeFromCache = excludeFromCache.any { originalRequest.url.toString().contains(it, ignoreCase = true) }

                // Create a new request builder, adding the API key header
                val newRequestBuilder = originalRequest.newBuilder()
                    .header("X-API-Key", API_KEY)

                // If the request should be excluded from cache, add the no-cache header
                if (shouldExcludeFromCache) {
                    newRequestBuilder.header("Cache-Control", "no-cache")
                }

                // Build the new request
                val newRequest = newRequestBuilder.build()

                // Proceed with the new request
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

}