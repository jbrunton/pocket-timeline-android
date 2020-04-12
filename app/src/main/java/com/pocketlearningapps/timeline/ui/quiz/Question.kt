package com.pocketlearningapps.timeline.ui.quiz

import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import java.time.LocalDate

sealed class Question {
    data class WhatDateQuestion(
        val timeline: Timeline,
        val event: Event
    ) : Question()

    data class WhichEventQuestion(
        val timeline: Timeline,
        val event: Event,
        val date: LocalDate,
        val options: List<Event>
    )
}

fun Question.WhatDateQuestion.validate(year: String): Boolean {
    return event.date.year.toString() == year
}

fun Question.WhichEventQuestion.validate(eventId: String): Boolean {
    return event.id == eventId
}
