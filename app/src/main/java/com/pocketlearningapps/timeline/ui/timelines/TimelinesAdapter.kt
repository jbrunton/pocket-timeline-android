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

typealias OnTimelineClickHandler = (timeline: Timeline) -> Unit

private val medalColors = mapOf(
    1 to R.color.colorBronze,
    2 to R.color.colorSilver,
    3 to R.color.colorGold
)

class TimelinesAdapter : RecyclerView.Adapter<TimelinesAdapter.ViewHolder>() {

    private val data = mutableListOf<Timeline>()

    var onTimelineClicked: OnTimelineClickHandler? = null

    fun setData(items: Collection<Timeline>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline, parent, false)

        view.setOnClickListener {
            val position = it.tag as Int
            val timeline = data.get(position)
            onTimelineClicked?.invoke(timeline)
        }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeline = data.get(position)
        holder.itemView.tag = position
        holder.title.text = timeline.title
        holder.description.text = timeline.description
        val medalColorRes = medalColors[timeline.level]
        if (medalColorRes != null) {
            holder.medal.isVisible = true
            val medalColor = getColor(holder.medal.context, medalColorRes)
            val medalColorStateList = getColorStateList(holder.medal.context, medalColorRes)
            holder.medal.setColorFilter(medalColor, SRC_IN)
            holder.medal.backgroundTintList = medalColorStateList
        } else {
            holder.medal.isVisible = false
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        val title: TextView = itemView.findViewById(R.id.timeline_title)
        var description: TextView = itemView.findViewById(R.id.timeline_description)
        val medal: ImageView = itemView.findViewById(R.id.medal)
    }
}
