package com.pocketlearningapps.timeline.ui.timelines

import android.content.Intent
import android.graphics.PorterDuff.Mode.SRC_IN
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getColorStateList
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

typealias OnQuizOptionClickHandler = (timeline: Timeline, level: Int) -> Unit

class QuizOptionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    private val data = mutableListOf<Timeline>()
    private val timelineViewHolderFactory = TimelineViewHolderFactory()

    var onTimelineClicked: OnTimelineClickHandler? = null

    fun setData(items: Collection<Timeline>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline, parent, false)

            return timelineViewHolderFactory.createViewHolder(view)

        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quiz_level, parent, false)

            return ItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return data.size * 4 // 3 levels + 1 header for each item
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 4 == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val timelineIndex = position / 4
        val timeline = data.get(timelineIndex)

        if (holder is TimelineViewHolder) {
            timelineViewHolderFactory.bindHolder(holder, timeline, data, timelineIndex)
        } else if (holder is ItemViewHolder) {
            val level = position % 4
            holder.levelName.text = "Level ${level}"

            val locked = level > timeline.level
            if (locked) {
                holder.lockIcon.isVisible = true
                holder.rating.isVisible = false
            } else {
                holder.lockIcon.isVisible = false
                holder.rating.isVisible = true
                holder.rating.rating = timeline.levelRating(level)?.starRating ?: 0f
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelName: TextView = itemView.findViewById(R.id.level_name)
        val rating: AppCompatRatingBar = itemView.findViewById(R.id.rating)
        val lockIcon: ImageView = itemView.findViewById(R.id.lock_icon)
    }
}
