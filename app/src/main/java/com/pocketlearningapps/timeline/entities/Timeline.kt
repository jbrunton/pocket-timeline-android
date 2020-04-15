package com.pocketlearningapps.timeline.entities

data class Timeline(
    val id: String,
    val title: String,
    val description: String?,
    val events: List<Event>?,
    val ratings: List<Rating>?
) {
    val level: Int get() {
        return ratings
            ?.filter { it.normalizedScore ?: 0f >= 0.75f }
            ?.map { it.level }
            ?.max() ?: 0
    }

    fun levelRating(level: Int): Rating {
        return ratings?.find { it.level == level } ?: Rating(level, null, false)
    }
}
