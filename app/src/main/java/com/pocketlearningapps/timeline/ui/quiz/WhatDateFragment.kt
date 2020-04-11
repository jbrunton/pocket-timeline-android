package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.lib.requireStringArgument
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_what_date.*
import kotlin.random.Random

class WhatDateFragment : Fragment(R.layout.fragment_what_date), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }
    private val timelineId by lazy { requireStringArgument("TIMELINE_ID") }
    private val service: RetrofitService by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            val timeline = service.timeline(timelineId)
            val event = timeline.events.get(Random.nextInt(timeline.events.size))
            timeline_title.text = timeline.title
            event_title.text = event.title
        }
    }

    companion object {
        fun build(timelineId: String) = WhatDateFragment().apply {
            arguments = Bundle().apply { putString("TIMELINE_ID", timelineId) }
        }
    }
}