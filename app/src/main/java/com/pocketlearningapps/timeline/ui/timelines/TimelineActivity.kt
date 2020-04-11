package com.pocketlearningapps.timeline.ui.timelines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : AppCompatActivity(R.layout.activity_timeline) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = sampleTimeline.title
        timeline_description.text = sampleTimeline.description

        val adapter = EventsAdapter().apply {
            setData(sampleTimeline.events)
        }
        timeline_events.adapter = adapter
        timeline_events.layoutManager = LinearLayoutManager(this)
    }
}
