package com.pocketlearningapps.timeline.ui.timelines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_timelines.*
import kotlinx.coroutines.async
import retrofit2.HttpException

class TimelinesFragment : Fragment(R.layout.fragment_timelines), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }

    private val service: RetrofitService by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val adapter = TimelinesAdapter()
        timelines.adapter = adapter
        timelines.layoutManager = LinearLayoutManager(activity)
        adapter.onTimelineClicked = this::onTimelineClicked

        lifecycleScope.launchWhenStarted {
            try {
                val timelines = service.timelines()
                adapter.setData(timelines)
            } catch (e: HttpException) {
                Log.d("HttpError", "Code: " + e.code())
            }
        }
    }

    private fun onTimelineClicked(timeline: Timeline) {
        val intent = Intent(requireContext(), TimelineActivity::class.java).apply {
            putExtra("TIMELINE_ID", timeline.id)
        }
        requireContext().startActivity(intent)
    }
}
