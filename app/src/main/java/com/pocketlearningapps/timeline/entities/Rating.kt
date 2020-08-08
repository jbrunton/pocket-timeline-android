package com.pocketlearningapps.timeline.entities

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.pocketlearningapps.timeline.R
import java.io.Serializable
import kotlin.math.max

enum class Medal(@ColorRes val color: Int, @DrawableRes val background: Int) {
    BRONZE(R.color.colorBronze, R.drawable.medal_background_bronze),
    SILVER(R.color.colorSilver, R.drawable.medal_background_silver),
    GOLD(R.color.colorGold, R.drawable.medal_background_gold);

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
    @SerializedName("normalized_score") val normalizedScore: Float?,
    val unlocked: Boolean
) : Serializable {
    val gpa: Float? get() {
        return normalizedScore?.let { it * 5 }
    }

    val gpaString: String? get() {
        return gpa?.let { "GPA %.1f".format(it) }
    }
}
