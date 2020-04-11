package com.pocketlearningapps.timeline.ui.timelines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import kotlinx.android.synthetic.main.fragment_timelines.*
import java.time.LocalDate

private val sampleTimeline = Timeline(
    title = "World War 2",
    description = "Events of World War 2",
    events = listOf(
        Event("Germany invades Poland", LocalDate.of(1939, 9, 1)),
        Event("Britain goes to war", LocalDate.of(1939, 9, 3))
    )
)

class TimelinesFragment : Fragment(R.layout.fragment_timelines) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title.text = sampleTimeline.title
        description.text = sampleTimeline.description

        val adapter = EventsAdapter().apply {
            setData(sampleTimeline.events)
        }
        events.adapter = adapter
        events.layoutManager = LinearLayoutManager(activity)
    }
}
