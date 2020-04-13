package com.pocketlearningapps.timeline.entities

import kotlin.random.Random

private const val QUIZ_LENGTH = 3

class Quiz(private val timeline: Timeline) {
    var totalQuestions = 0
        private set

    var correctQuestions = 0
        private set

    lateinit var currentQuestion: Question
        private set

    val isComplete get() = correctQuestions >= QUIZ_LENGTH

    fun nextQuestion(): Question {
        if (isComplete) {
            throw IllegalStateException("Quiz is complete")
        }

        val event = timeline.events.get(Random.nextInt(timeline.events.size))
        currentQuestion = if (false /*Random.nextBoolean()*/) {
            Question.WhatDateQuestion(timeline, event)
        } else {
            Question.WhichEventQuestion(timeline, event, pickEventOptions(timeline.events, event))
        }

        return currentQuestion
    }

    fun submitAnswer(answer: Any?): Boolean {
        val correct = currentQuestion.validate(answer)
        if (correct) {
            correctQuestions += 1
        }
        totalQuestions += 1
        if (!isComplete) {
            nextQuestion()
        }
        return correct
    }

    private fun pickEventOptions(events: List<Event>, correctAnswer: Event): List<Event> {
        return events
            .filter { it.id != correctAnswer.id }
            .sortedBy { Random.nextInt() }
            .take(2)
            .plus(correctAnswer)
    }
}
