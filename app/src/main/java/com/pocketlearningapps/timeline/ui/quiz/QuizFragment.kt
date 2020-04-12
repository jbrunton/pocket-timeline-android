package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jbrunton.inject.*
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuizFragment : Fragment(R.layout.fragment_quiz), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }
    private val viewModel: QuizViewModel by injectViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { updateViewState(it) })

        submit.setOnClickListener { viewModel.nextQuestion() }
    }

    private fun updateViewState(viewState: QuizViewState) {
        question.text = viewState.question
        timeline_title.text = viewState.timelineTitle

        what_date_content.isVisible = viewState.showWhatDateContent
        event_title.text = viewState.whatDateContent.eventTitle

        which_event_content.isVisible = viewState.showWhichEventContent
    }
}

