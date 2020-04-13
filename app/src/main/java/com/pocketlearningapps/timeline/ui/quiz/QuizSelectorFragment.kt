package com.pocketlearningapps.timeline.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.network.RetrofitService
import com.pocketlearningapps.timeline.ui.timelines.TimelineActivity
import com.pocketlearningapps.timeline.ui.timelines.TimelinesAdapter
import kotlinx.android.synthetic.main.fragment_quiz_selector.*

class QuizSelectorFragment : Fragment(R.layout.fragment_quiz_selector), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }

    private val service: RetrofitService by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TimelinesAdapter()
        timelines.adapter = adapter
        timelines.layoutManager = LinearLayoutManager(context)
        adapter.onTimelineClicked = this::onTimelineClicked

        lifecycleScope.launchWhenCreated {
            val timelines = service.timelines()
            adapter.setData(timelines)
        }
    }

    private fun onTimelineClicked(timeline: Timeline) {
        val intent = Intent(requireContext(), QuizActivity::class.java).apply {
            putExtra("TIMELINE_ID", id)
        }
        requireContext().startActivity(intent)
    }
}
