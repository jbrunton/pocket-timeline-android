package com.pocketlearningapps.timeline.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Category
import com.pocketlearningapps.timeline.entities.Medal
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.ui.timelines.TimelineViewHolder
import com.pocketlearningapps.timeline.ui.timelines.TimelineViewHolderFactory

typealias OnQuizOptionClickHandler = (category: Category, level: Int) -> Unit

class QuizOptionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        internal const val TYPE_HEADER = 0
        internal const val TYPE_ITEM = 1
    }

    internal sealed class QuizOptionItem {
        abstract val adapterType: Int

        data class Header(val timeline: Timeline) : QuizOptionItem() {
            override val adapterType =
                TYPE_HEADER
        }

        data class Item(val category: Category) : QuizOptionItem() {
            override val adapterType =
                TYPE_ITEM
        }
    }



    private val data = mutableListOf<QuizOptionItem>()
    private val timelineViewHolderFactory =
        TimelineViewHolderFactory()

    var onQuizOptionClicked: OnQuizOptionClickHandler? = null

    fun setData(items: Collection<Timeline>) {
        this.data.clear()
        items.forEach { timeline ->
            this.data.add(
                QuizOptionItem.Header(
                    timeline
                )
            )
            val categories = timeline.categories
            if (categories != null) {
                this.data.addAll(categories.map {
                    QuizOptionItem.Item(
                        it
                    )
                })
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
                val item = data.get(position) as QuizOptionItem.Item
                onQuizOptionClicked?.invoke(item.category, item.category.level)
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
            val category = item.category
            holder.itemView.tag = position
            holder.categoryName.text = category.name

            val context = holder.itemView.context

            listOf(1, 2, 3).forEach { level ->
                val icon = holder.iconForLevel(level)
                val rating = category.levelRating(level)
                val greyColorList = ContextCompat.getColorStateList(context, R.color.colorGreyLight)
                if (rating.unlocked) {
                    val medal = Medal.forGpa(rating.gpa)
                    if (medal != null) {
                        val medalColorList = ContextCompat.getColorStateList(context, medal.color)
                        val medalBackround = ContextCompat.getDrawable(context, medal.background)
                        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medal_24dp))
                        icon.background = medalBackround
                        icon.imageTintList = medalColorList
                        icon.backgroundTintList = null
                    } else {
                        val accentColorList = ContextCompat.getColorStateList(context, R.color.colorAccent)
                        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_black_24dp))
                        icon.background = ContextCompat.getDrawable(context, R.drawable.medal_border)
                        icon.imageTintList = accentColorList
                        icon.backgroundTintList = greyColorList
                    }
                } else {
                    icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_black_24dp))
                    icon.background = ContextCompat.getDrawable(context, R.drawable.medal_border)
                    icon.imageTintList = greyColorList
                    icon.backgroundTintList = greyColorList
                }
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val iconLevel1: ImageView = itemView.findViewById(R.id.icon_level_1)
        val iconLevel2: ImageView = itemView.findViewById(R.id.icon_level_2)
        val iconLevel3: ImageView = itemView.findViewById(R.id.icon_level_3)

        fun iconForLevel(level: Int) = when (level) {
            1 -> iconLevel1
            2 -> iconLevel2
            3 -> iconLevel3
            else -> throw IllegalArgumentException("Unexpected level: $level")
        }
    }
}
