package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuizFragment : Fragment(R.layout.fragment_quiz), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }
    private lateinit var timelines: List<Timeline>
    private val service: RetrofitService by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        submit.setOnClickListener { nextQuestion() }

        lifecycleScope.launch {
            timelines = service.timelines()
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        val timeline = timelines.get(Random.nextInt(timelines.size))
        val fragment = WhatDateFragment.build(timeline.id)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.question_content, fragment)
            .commit()
    }
}

