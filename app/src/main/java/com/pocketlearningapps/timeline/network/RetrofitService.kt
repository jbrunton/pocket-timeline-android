package com.pocketlearningapps.timeline.network

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


data class ValidateTokenRequest(
    @SerializedName("id_token") val idToken: String?
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

