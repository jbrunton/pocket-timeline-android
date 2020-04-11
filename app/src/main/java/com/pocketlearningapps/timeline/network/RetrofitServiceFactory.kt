package com.pocketlearningapps.timeline.network

import com.google.gson.GsonBuilder
import com.pocketlearningapps.timeline.auth.session.SessionCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class RetrofitServiceFactory(
    private val cookieJar: SessionCookieJar
) {
    fun create(): RetrofitService {
        val client = OkHttpClient.Builder()
            .apply(::configureTimeout)
            .apply(::configureLogging)
            .apply(::configureCookies)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create(gson))
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
        builder.cookieJar(cookieJar)
    }
}