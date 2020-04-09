package com.pocketlearningapps.timeline.network

import android.content.Context
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


data class ValidateTokenRequest(
    val id_token: String?
)

data class UserResponse(
    val id: String?,
    val name: String?,
    val email: String?
)

interface RetrofitService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("auth/google_oauth2/signin")
    suspend fun validateToken(@Body request: ValidateTokenRequest): JsonElement

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("user/profile")
    suspend fun profile(): UserResponse
}

class RetrofitServiceFactory(private val context: Context) {
    fun create(): RetrofitService {
        val client = OkHttpClient.Builder()
            .apply(::configureTimeout)
            .apply(::configureLogging)
            .apply(::configureCookies)
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

    private fun configureCookies(builder: OkHttpClient.Builder) {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        builder.cookieJar(cookieJar)
    }

    companion object {
        lateinit var instance: RetrofitService
            private set

        fun initialize(context: Context) {
            if (!this::instance.isInitialized) {
                instance = RetrofitServiceFactory(context).create()
            }
        }
    }
}
