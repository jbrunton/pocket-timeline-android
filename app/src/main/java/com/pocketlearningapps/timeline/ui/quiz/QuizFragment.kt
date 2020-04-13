package com.pocketlearningapps.timeline.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start_quiz.setOnClickListener {
            startActivity(Intent(context, QuizActivity::class.java))
        }
    }
}
