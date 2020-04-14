package com.pocketlearningapps.timeline.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.entities.withRatings
import com.pocketlearningapps.timeline.network.RetrofitService
import com.pocketlearningapps.timeline.ui.timelines.TimelineActivity
import com.pocketlearningapps.timeline.ui.timelines.TimelinesAdapter
import kotlinx.android.synthetic.main.fragment_quiz_selector.*
import kotlinx.coroutines.async
import retrofit2.HttpException

private const val REQUEST_CODE = 0x10

class QuizSelectorFragment : Fragment(R.layout.fragment_quiz_selector), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }

    private val service: RetrofitService by inject()
    private lateinit var adapter: TimelinesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TimelinesAdapter()
        timelines.adapter = adapter
        timelines.layoutManager = LinearLayoutManager(context)
        adapter.onTimelineClicked = this::onTimelineClicked

        refreshData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            refreshData()
        }
    }

    private fun onTimelineClicked(timeline: Timeline) {
        val intent = Intent(requireContext(), QuizActivity::class.java).apply {
            putExtra("TIMELINE_ID", timeline.id)
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun refreshData() {
        lifecycleScope.launchWhenCreated {
            try {
                val timelines = async { service.timelines() }
                val ratings = async { service.ratings() }
                adapter.setData(timelines.await().withRatings(ratings.await()))
            } catch (e: HttpException) {
                Log.d("HttpError", "Code: " + e.code())
            }
        }
    }
}
