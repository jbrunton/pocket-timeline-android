package com.pocketlearningapps.timeline.ui.quiz

import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import java.time.LocalDate

sealed class Question {
    abstract fun validate(answer: String): Boolean
    abstract val correctAnswer: String

    data class WhatDateQuestion(
        val timeline: Timeline,
        val event: Event
    ) : Question() {
        override fun validate(answer: String) = answer == correctAnswer
        override val correctAnswer: String = event.date.year.toString()
    }

    data class WhichEventQuestion(
        val timeline: Timeline,
        val event: Event,
        val options: List<Event>
    ) : Question() {
        override fun validate(answer: String) = answer == event.id
        override val correctAnswer: String = event.title
    }
}

