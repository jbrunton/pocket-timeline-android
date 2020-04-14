package com.pocketlearningapps.timeline.entities

import com.google.gson.annotations.SerializedName
import kotlin.math.max
import kotlin.math.roundToInt

val sampleRatings = listOf(
    Rating(1, 0.85f),
    Rating(2, 0.82f),
    Rating(3, 0.4f)
)

data class Timeline(
    val id: String,
    val title: String,
    val description: String?,
    val events: List<Event>?,
    val ratings: List<Rating>?
) {
    val level: Int get() {
        return (ratings ?: sampleRatings)
            .filter { it.normalizedScore ?: 0f >= 0.8f }
            .map { it.level }
            .max() ?: 0
    }

    fun levelRating(level: Int): Rating? {
        return (ratings ?: sampleRatings).find { it.level == level }
    }
}
