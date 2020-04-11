package com.pocketlearningapps.timeline.ui.timelines

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline

class TimelinesAdapter : RecyclerView.Adapter<TimelinesAdapter.ViewHolder>() {
    private val data = mutableListOf<Timeline>()

    fun setData(items: Collection<Timeline>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline, parent, false)

        view.setOnClickListener {
            view.context.startActivity(Intent(view.context, TimelineActivity::class.java))
        }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeline = data.get(position)
        holder.title.text = timeline.title
        holder.description.text = timeline.description
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        val title: TextView = itemView.findViewById(R.id.timeline_title)
        var description: TextView = itemView.findViewById(R.id.timeline_description)
    }
}