package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlin.random.Random

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        submit.setOnClickListener { nextQuestion() }

        nextQuestion()
    }

    private fun nextQuestion() {
        val fragment = if (Random.nextBoolean()) { WhatDateFragment() } else { WhichEventFragment() }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.question_content, fragment)
            .commit()
    }
}
