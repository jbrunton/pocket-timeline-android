package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.ui.timelines.sampleTimeline
import kotlinx.android.synthetic.main.fragment_what_date.*

class WhatDateFragment : Fragment(R.layout.fragment_what_date) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeline = sampleTimeline
        val event = timeline.events.first()
        timeline_title.text = timeline.title
        event_title.text = event.title
    }
}