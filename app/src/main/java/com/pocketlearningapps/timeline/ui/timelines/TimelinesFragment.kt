package com.pocketlearningapps.timeline.ui.timelines

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_timelines.*

class TimelinesFragment : Fragment(R.layout.fragment_timelines) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val adapter = TimelinesAdapter().apply {
            setData(listOf(sampleTimeline))
        }
        timelines.adapter = adapter
        timelines.layoutManager = LinearLayoutManager(activity)
    }
}
