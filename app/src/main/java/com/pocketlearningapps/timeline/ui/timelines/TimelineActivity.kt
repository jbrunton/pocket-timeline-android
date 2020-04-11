package com.pocketlearningapps.timeline.ui.timelines

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : AppCompatActivity(R.layout.activity_timeline) {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = sampleTimeline.title
        timeline_description.text = sampleTimeline.description

        val adapter = EventsAdapter().apply {
            setData(sampleTimeline.events)
        }
        timeline_events.adapter = adapter
        timeline_events.layoutManager = LinearLayoutManager(this)
    }
}
