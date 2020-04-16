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
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Medal
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.lib.ViewHolderFactory
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

typealias OnTimelineClickHandler = (timeline: Timeline) -> Unit

class TimelinesAdapter : RecyclerView.Adapter<TimelineViewHolder>() {
    private val data = mutableListOf<Timeline>()
    private val factory = TimelineViewHolderFactory()

    var onTimelineClicked: OnTimelineClickHandler? = null

    fun setData(items: Collection<Timeline>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline, parent, false)

        view.setOnClickListener {
            val position = it.tag as Int
            val timeline = data.get(position)
            onTimelineClicked?.invoke(timeline)
        }

        return factory.createViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val timeline = data.get(position)
        factory.bindHolder(holder, timeline, position)
    }
}

class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.timeline_title)
    var description: TextView = itemView.findViewById(R.id.timeline_description)
}

class TimelineViewHolderFactory : ViewHolderFactory<Timeline, TimelineViewHolder> {
    override fun createViewHolder(view: View): TimelineViewHolder {
        return TimelineViewHolder(view)
    }

    override fun bindHolder(
        holder: TimelineViewHolder,
        item: Timeline,
        position: Int
    ) {
        holder.itemView.tag = position
        holder.title.text = item.title
        holder.description.text = item.description
    }
}
