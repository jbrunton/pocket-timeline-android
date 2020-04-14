package com.pocketlearningapps.timeline.network

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.pocketlearningapps.timeline.entities.Timeline
import retrofit2.http.*


data class ValidateTokenRequest(
    @SerializedName("id_token") val idToken: String?
)

data class UserResponse(
    val id: String?,
    val name: String?,
    val email: String?
)

data class Rating(
    @SerializedName("timeline_id") val timelineId: String,
    @SerializedName("normalized_score") val normalizedScore: Float
)

interface RetrofitService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/auth/google_oauth2/signin")
    suspend fun validateToken(@Body request: ValidateTokenRequest): JsonElement

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/user/profile")
    suspend fun profile(): UserResponse

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/timelines")
    suspend fun timelines(): List<Timeline>

    @GET("/timelines/{id}")
    suspend fun timeline(@Path("id") id: String): Timeline

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/ratings/score")
    suspend fun ratingsScore(@Body rating: Rating): JsonElement

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/ratings")
    suspend fun ratings(): List<Rating>
}
