package com.pocketlearningapps.timeline.entities

import com.google.gson.annotations.SerializedName
import kotlin.math.max
import kotlin.math.roundToInt

data class Timeline(
    val id: String,
    val title: String,
    val description: String?,
    val events: List<Event>?,
    @SerializedName("normalized_score") val normalizedScore: Float?
) {
    val starRating: Float get() {
        return if (normalizedScore == null) {
            // 0 means no rating
            0f
        } else {
            // otherwise, a 1-5 rating
            max( normalizedScore * 5, 1f)
        }
    }
}