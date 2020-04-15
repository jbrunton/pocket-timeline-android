package com.pocketlearningapps.timeline.ui.timelines

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
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

            val locked = level > timeline.level + 1
            val context = holder.itemView.context
            if (locked) {
                holder.icon.visibility = View.VISIBLE
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_black_24dp))
                holder.icon.background = null
                holder.icon.backgroundTintList = null
                holder.icon.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray))
                holder.gpa.isVisible = false
            } else {
                val rating = timeline.levelRating(level)
                if (rating.gpa != null) {
                    holder.gpa.isVisible = true
                    holder.gpa.text = rating.gpaString
                } else {
                    holder.gpa.isVisible = false
                }

                val medal = Medal.forGpa(rating.gpa)
                if (medal != null) {
                    val medalColor = ContextCompat.getColor(context, medal.color)
                    val medalColorStateList = ContextCompat.getColorStateList(context, medal.color)
                    val whiteColor = ContextCompat.getColor(context, android.R.color.white)
                    val whiteColorStateList = ContextCompat.getColorStateList(context, android.R.color.white)

                    holder.levelName.setTextColor(whiteColor)
                    holder.gpa.chipBackgroundColor = whiteColorStateList
                    holder.gpa.setTextColor(medalColor)

                    holder.itemView.background = ContextCompat.getDrawable(context, R.drawable.rounded_border)
                    holder.itemView.backgroundTintList = medalColorStateList

                    holder.icon.visibility = View.VISIBLE
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medal_24dp))
                    holder.icon.background = ContextCompat.getDrawable(context, R.drawable.rounded_border)
                    holder.icon.setColorFilter(medalColor, PorterDuff.Mode.SRC_IN)
                    holder.icon.backgroundTintList = whiteColorStateList
                } else {
                    holder.icon.visibility = View.INVISIBLE
                }
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelName: TextView = itemView.findViewById(R.id.level_name)
        val gpa: Chip = itemView.findViewById(R.id.gpa)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }
}
