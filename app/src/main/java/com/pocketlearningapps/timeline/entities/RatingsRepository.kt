package com.pocketlearningapps.timeline.entities

import kotlin.math.roundToInt

private const val MAX_RATINGS = 5

class RatingsRepository {
    private val ratingsCache = mutableMapOf<String, MutableList<Int>>()

    fun addScore(timelineId: String, percentCorrect: Int) {
        val ratings = ratingsFor(timelineId)
        ratings.add(percentCorrect)
        while (ratings.size >= MAX_RATINGS) {
            ratings.removeAt(0)
        }
    }

    fun getRating(timelineId: String): Int {
        val ratings = ratingsFor(timelineId)
        val score = if (ratings.size > 0) {
            (5.0 * ratings.average() / 100.0).roundToInt()
        } else {
            0
        }
        return score
    }

    private fun ratingsFor(timelineId: String) =
        ratingsCache.getOrPut(timelineId) { mutableListOf() }
}
