package com.example.apptshirt

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Gunakan IP yang benar sesuai dengan server Anda
    private const val BASE_URL = "http://10.0.2.2:3001/api/"

    // Buat logging interceptor untuk debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Konfigurasi OkHttpClient dengan timeout yang lebih lama
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)       // Ditingkatkan menjadi 60 detik
        .readTimeout(60, TimeUnit.SECONDS)          // Ditingkatkan menjadi 60 detik
        .writeTimeout(60, TimeUnit.SECONDS)         // Ditingkatkan menjadi 60 detik
        .retryOnConnectionFailure(true)            // Enable retry
        .followRedirects(true)                     // Enable redirects
        .followSslRedirects(true)                  // Enable SSL redirects
        .build()

    // Buat instance Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create API service
    val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)
}