package com.pocketlearningapps.timeline.ui.timelines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Event
import kotlinx.android.synthetic.main.item_event.view.*


class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    private val data = mutableListOf<Event>()

    fun setData(items: Collection<Event>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)

        //view.setOnClickListener(clickListener)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = data.get(position)
        holder.title.text = event.title
        holder.date.text = event.date.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        val title: TextView = itemView.findViewById(R.id.event_title)
        var date: TextView = itemView.findViewById(R.id.event_date)
    }
}
