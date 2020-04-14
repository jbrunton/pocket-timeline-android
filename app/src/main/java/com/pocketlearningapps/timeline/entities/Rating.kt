package com.pocketlearningapps.timeline.entities

import com.google.gson.annotations.SerializedName
import kotlin.math.max

data class Rating(
    val level: Int,
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
