package com.pocketlearningapps.timeline.entities

import com.pocketlearningapps.timeline.network.Rating

data class Timeline(
    val id: String,
    val title: String,
    val description: String?,
    val events: List<Event>?,
    val rating: Float?
)

fun List<Timeline>.withRatings(ratings: List<Rating>): List<Timeline> {
    val cache = ratings.map { Pair(it.timelineId, it.normalizedScore) }.toMap()
    return map { timeline ->
        timeline.copy(rating = cache[timeline.id])
    }
}
