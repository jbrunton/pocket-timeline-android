package com.pocketlearningapps.timeline.entities

import com.pocketlearningapps.timeline.network.Score
import kotlin.math.roundToInt
import kotlin.random.Random

private const val QUIZ_LENGTH = 3

class Quiz(
    private val category: Category,
    private val level: Int
) {
    var totalQuestions = 0
        private set

    var correctQuestions = 0
        private set

    lateinit var currentQuestion: Question
        private set

    val isComplete get() = correctQuestions >= QUIZ_LENGTH

    val percentComplete: Int get() = ((correctQuestions + 1) * 100.0 / QUIZ_LENGTH).roundToInt()
    val normalizedScore: Float get() = correctQuestions.toFloat() / totalQuestions.toFloat()
    val percentageScore: Int get() = (normalizedScore * 100.0).roundToInt()

    fun nextQuestion(): Question {
        if (isComplete) {
            throw IllegalStateException("Quiz is complete")
        }

        val event = category.events!!.get(Random.nextInt(category.events.size))
        currentQuestion = if (level > 1) {
            val dateComponents = pickDateComponents(level)
            Question.WhatDateQuestion(category, event, dateComponents)
        } else {
            Question.WhichEventQuestion(category, event, pickEventOptions(category.events, event))
        }

        return currentQuestion
    }

    fun submitAnswer(answer: Any?, submitScore: (Score) -> Unit): Boolean {
        val correct = currentQuestion.validate(answer)
        if (correct) {
            correctQuestions += 1
        }
        totalQuestions += 1
        if (isComplete) {
            submitScore(Score(
                categoryId = category.id,
                level = level,
                normalizedScore = normalizedScore))
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

    private fun pickDateComponents(level: Int): List<DatePart> {
        return when (level) {
            2 -> {
                when (Random.nextInt(3)) {
                    0 -> listOf(DatePart.MONTH)
                    1 -> listOf(DatePart.YEAR)
                    else -> listOf(DatePart.MONTH, DatePart.YEAR)
                }
            }
            3 -> {
                when (Random.nextInt(4)) {
                    0 -> listOf(DatePart.DAY, DatePart.MONTH)
                    1 -> listOf(DatePart.DAY, DatePart.YEAR)
                    2 -> listOf(DatePart.MONTH, DatePart.YEAR)
                    else -> listOf(DatePart.DAY, DatePart.MONTH, DatePart.YEAR)
                }
            }
            else -> throw IllegalArgumentException("Unexpected level: $level")
        }
    }
}
