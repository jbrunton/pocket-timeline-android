package com.pocketlearningapps.timeline.entities

import java.io.Serializable

data class Category(
    val id: String,
    val name: String,
    val ratings: List<Rating>?,
    val events: List<Event>?,
    val timeline: Timeline?
) : Serializable {
    val level: Int get() {
        return ratings
            ?.filter { it.unlocked }
            ?.map { it.level }
            ?.max() ?: 1
    }

    fun levelRating(level: Int): Rating {
        return ratings?.find { it.level == level } ?: Rating(level, null, false)
    }

    val description: String get() {
        return timeline?.let {
            "${timeline.title} / ${name}"
        } ?: name
    }
}
