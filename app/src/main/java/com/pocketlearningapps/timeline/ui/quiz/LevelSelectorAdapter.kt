package com.pocketlearningapps.timeline.ui.quiz

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
import com.pocketlearningapps.timeline.entities.Category
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Medal

typealias OnLevelSelectorClickListener = (level: Int) -> Unit

class LevelSelectorAdapter : RecyclerView.Adapter<LevelSelectorAdapter.ViewHolder>() {
    var clickListener: OnLevelSelectorClickListener? = null

    private lateinit var category: Category

    fun setCategory(category: Category) {
        this.category = category
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_level, parent, false)

        view.setOnClickListener {
            val position = view.tag as Int
            clickListener?.invoke(position + 1)
        }

        return ViewHolder(view)
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.itemView.tag = position

        val level = position + 1
        val rating = category.levelRating(level)

        holder.level.text = "Level $level"
        if (rating.unlocked) {
            holder.level.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            holder.level.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
        }

        val icon = holder.icon
        holder.gpa.setText(rating.gpaString)
        holder.gpa.isVisible = rating.normalizedScore != null
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
        //holder.icon.text = event.date.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        val level: TextView = itemView.findViewById(R.id.level_name)
        val gpa: Chip = itemView.findViewById(R.id.gpa)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }
}