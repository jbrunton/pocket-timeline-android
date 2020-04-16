package com.pocketlearningapps.timeline.entities

data class Category(
    val id: String,
    val name: String,
    val ratings: List<Rating>?,
    val events: List<Event>?
) {
    val level: Int get() {
        return ratings
            ?.filter { it.unlocked }
            ?.map { it.level }
            ?.max() ?: 1
    }

    fun levelRating(level: Int): Rating {
        return ratings?.find { it.level == level } ?: Rating(level, null, false)
    }
}
