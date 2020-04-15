package com.pocketlearningapps.timeline.entities

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import com.pocketlearningapps.timeline.R
import kotlin.math.max

enum class Medal(@ColorRes val color: Int) {
    BRONZE(R.color.colorBronze),
    SILVER(R.color.colorSilver),
    GOLD(R.color.colorGold);

    companion object {
        fun forGpa(gpa: Float?) = when {
            gpa == null -> null
            gpa >= 4.5 -> GOLD
            gpa >= 4.0 -> SILVER
            gpa >= 3.5 -> BRONZE
            else -> null
        }
    }
}

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

    val gpa: Float? get() {
        return normalizedScore?.let { it * 5 }
    }

    val gpaString: String get() {
        return "GPA " + (gpa?.let { "%.1f".format(it) } ?: "-")
    }
}
