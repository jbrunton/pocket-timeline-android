package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        nextQuestion()
    }

    fun nextQuestion() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.question_content, WhatDateFragment())
            .commit()
    }
}
