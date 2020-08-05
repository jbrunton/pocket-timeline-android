package com.pocketlearningapps.timeline.ui.timelines

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : AppCompatActivity(R.layout.activity_timeline), HasContainer {
    override val container by lazy { (application as HasContainer).container }

    private val timelineId by lazy { intent.getStringExtra("TIMELINE_ID") }
    private val service: RetrofitService by inject()

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

        val adapter = EventsAdapter()
        timeline_events.adapter = adapter
        timeline_events.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launchWhenStarted {
            val timeline = service.timeline(timelineId)
            title = timeline.title
            timeline_description.text = timeline.description
            adapter.setData(timeline.events!!)
        }
    }
}
