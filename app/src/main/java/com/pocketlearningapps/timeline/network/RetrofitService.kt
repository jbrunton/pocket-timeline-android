package com.pocketlearningapps.timeline.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

data class ValidateTokenRequest(
    @SerializedName("id_token") val idToken: String?
)

interface RetrofitService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("auth/google_oauth2/signin")
    suspend fun validateToken(@Body request: ValidateTokenRequest): JsonElement
}

class RetrofitServiceFactory {
    fun create(): RetrofitService {
        val client = OkHttpClient.Builder()
            .apply(::configureTimeout)
            .apply(::configureLogging)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RetrofitService::class.java)
    }

    private fun configureTimeout(builder: OkHttpClient.Builder) {
        builder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
    }

    private fun configureLogging(builder: OkHttpClient.Builder) {
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
    }
}
