package com.pocketlearningapps.timeline.entities

import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random

private const val QUIZ_LENGTH = 3

class Quiz(
    private val timeline: Timeline,
    private val ratingsRepository: RatingsRepository
) {
    var totalQuestions = 0
        private set

    var correctQuestions = 0
        private set

    lateinit var currentQuestion: Question
        private set

    val isComplete get() = correctQuestions >= QUIZ_LENGTH

    val percentComplete: Int get() = ((correctQuestions + 1) * 100.0 / QUIZ_LENGTH).roundToInt()
    val percentCorrect: Int get() = (correctQuestions * 100.0 / totalQuestions).roundToInt()

    fun nextQuestion(): Question {
        if (isComplete) {
            throw IllegalStateException("Quiz is complete")
        }

        val event = timeline.events.get(Random.nextInt(timeline.events.size))
        currentQuestion = if (Random.nextBoolean()) {
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
        if (isComplete) {
            ratingsRepository.addScore(timeline.id, percentCorrect)
        }
        else {
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
