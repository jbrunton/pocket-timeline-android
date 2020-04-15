package com.pocketlearningapps.timeline.ui.timelines

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Category
import com.pocketlearningapps.timeline.entities.Medal
import com.pocketlearningapps.timeline.entities.Timeline

typealias OnQuizOptionClickHandler = (timeline: Timeline, level: Int) -> Unit

class QuizOptionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        internal const val TYPE_HEADER = 0
        internal const val TYPE_ITEM = 1
    }

    internal sealed class QuizOptionItem {
        abstract val adapterType: Int

        data class Header(val timeline: Timeline) : QuizOptionItem() {
            override val adapterType = TYPE_HEADER
        }

        data class Item(val category: Category) : QuizOptionItem() {
            override val adapterType = TYPE_ITEM
        }
    }



    private val data = mutableListOf<QuizOptionItem>()
    private val timelineViewHolderFactory = TimelineViewHolderFactory()

    var onQuizOptionClicked: OnQuizOptionClickHandler? = null

    fun setData(items: Collection<Timeline>) {
        this.data.clear()
        items.forEach { timeline ->
            this.data.add(QuizOptionItem.Header(timeline))
            val categories = timeline.categories
            if (categories != null) {
                this.data.addAll(categories.map { QuizOptionItem.Item(it) })
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline, parent, false)

            return timelineViewHolderFactory.createViewHolder(view)

        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quiz_category, parent, false)

            view.setOnClickListener {
                val position = view.tag as Int
                val timelineIndex = position / 4
                val timeline = data.get(timelineIndex)
                val level = position % 4
                onQuizOptionClicked?.invoke(timeline, level)
            }

            return ItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data.get(position).adapterType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TimelineViewHolder) {
            val header = data.get(position) as QuizOptionItem.Header
            val timeline = header.timeline
            timelineViewHolderFactory.bindHolder(holder, timeline, position)
        } else if (holder is ItemViewHolder) {
            val item = data.get(position) as QuizOptionItem.Item
            holder.itemView.tag = position
            holder.categoryName.text = item.category.name
            val level = item.category.level

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
                    val medalColor = ContextCompat.getColor(context, medal.color)
                    val medalBackround = holder.icon.background as GradientDrawable
                    medalBackround.colors = intArrayOf(
                        medalColor,
                        ColorUtils.blendARGB(medalColor, Color.WHITE, 0.5f),
                        medalColor
                    )

                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medal_24dp))
                    holder.icon.background = medalBackround
                } else {
                    holder.icon.visibility = View.INVISIBLE
                }
                holder.gpa.text = rating.gpaString
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }
}
