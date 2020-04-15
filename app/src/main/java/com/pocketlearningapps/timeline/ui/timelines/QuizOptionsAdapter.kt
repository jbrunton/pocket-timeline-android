package com.pocketlearningapps.timeline.ui.timelines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Medal
import com.pocketlearningapps.timeline.entities.Timeline

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

            val context = holder.itemView.context

            val locked = level > timeline.level + 1
            holder.icon.visibility = View.VISIBLE
            if (locked) {
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_black_24dp))
                holder.icon.backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.white)
                holder.icon.imageTintList = ContextCompat.getColorStateList(context, android.R.color.darker_gray)
                holder.gpa.isVisible = false
            } else {
                holder.gpa.isVisible = true
                val rating = timeline.levelRating(level)
                val medal = Medal.forGpa(rating.gpa)
                if (medal != null) {
                    val medalColorStateList = ContextCompat.getColorStateList(context, medal.color)
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medal_24dp))
                    holder.icon.backgroundTintList = medalColorStateList
                } else {
                    holder.icon.visibility = View.INVISIBLE
                }
                holder.gpa.text = rating.gpaString
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelName: TextView = itemView.findViewById(R.id.level_name)
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val gpa: Chip = itemView.findViewById(R.id.gpa)
    }
}
