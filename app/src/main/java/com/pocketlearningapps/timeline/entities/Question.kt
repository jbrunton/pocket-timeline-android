package com.pocketlearningapps.timeline.entities

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

enum class DatePart {
    DAY,
    MONTH,
    YEAR
}

sealed class Question {
    abstract fun validate(answer: Any?): Boolean
    abstract val correctAnswer: String

    data class WhatDateQuestion(
        val category: Category,
        val event: Event,
        val dateComponents: List<DatePart>
    ) : Question() {
        override fun validate(answer: Any?) = answer == event.date
        override val correctAnswer: String = event.date.format(formatter)
    }

    data class WhichEventQuestion(
        val category: Category,
        val event: Event,
        val options: List<Event>
    ) : Question() {
        override fun validate(answer: Any?) = answer == event.id
        override val correctAnswer: String = event.title
    }
}
