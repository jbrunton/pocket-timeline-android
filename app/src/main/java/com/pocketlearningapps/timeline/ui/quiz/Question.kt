package com.pocketlearningapps.timeline.ui.quiz

import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

sealed class Question {
    abstract fun validate(answer: Any?): Boolean
    abstract val correctAnswer: String

    data class WhatDateQuestion(
        val timeline: Timeline,
        val event: Event
    ) : Question() {
        override fun validate(answer: Any?) = answer == correctAnswer
        override val correctAnswer: String = event.date.format(formatter)
    }

    data class WhichEventQuestion(
        val timeline: Timeline,
        val event: Event,
        val options: List<Event>
    ) : Question() {
        override fun validate(answer: Any?) = answer == event.id
        override val correctAnswer: String = event.title
    }
}

