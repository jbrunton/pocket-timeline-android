package com.pocketlearningapps.timeline.ui.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketlearningapps.timeline.entities.DatePart
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Question
import com.pocketlearningapps.timeline.entities.Quiz
import com.pocketlearningapps.timeline.lib.SingleLiveAction
import com.pocketlearningapps.timeline.lib.SingleLiveEvent
import com.pocketlearningapps.timeline.network.Score
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class WhatDateViewState(
    val showError: Boolean,
    val dayEditable: Boolean,
    val day: String,
    val monthEditable: Boolean,
    val month: String,
    val yearEditable: Boolean,
    val year: String
) {
    companion object {
        val Empty = WhatDateViewState(false, false, "", false, "", false, "")
    }
}

data class WhichEventViewState(
    val options: List<Event>,
    val uuid: UUID
)

data class QuizViewState(
    val questionTitle: String,
    val questionDetails: String,
    val categoryDescription: String,
    val showWhatDateContent: Boolean,
    val whatDateContent: WhatDateViewState,
    val showWhichEventContent: Boolean,
    val whichEventContent: WhichEventViewState,
    val submitEnabled: Boolean,
    val percentComplete: Int
)

class QuizViewStateFactory {
    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun viewState(question: Question, percentComplete: Int): QuizViewState {
        return when (question) {
            is Question.WhatDateQuestion -> QuizViewState(
                questionTitle = "When did the following event occur?",
                questionDetails = question.event.title,
                showWhatDateContent = true,
                whatDateContent = whatDateViewState(question),
                showWhichEventContent = false,
                whichEventContent = WhichEventViewState(emptyList(), UUID.randomUUID()),
                submitEnabled = false,
                percentComplete = percentComplete,
                categoryDescription = question.category.description
            )
            is Question.WhichEventQuestion -> QuizViewState(
                questionTitle = "Select the event that occurred on this date",
                questionDetails = question.event.date.format(dateFormatter),
                showWhatDateContent = false,
                whatDateContent = WhatDateViewState.Empty,
                showWhichEventContent = true,
                whichEventContent = WhichEventViewState(question.options, UUID.randomUUID()),
                submitEnabled = false,
                percentComplete = percentComplete,
                categoryDescription = question.category.description
            )
        }
    }

    private fun whatDateViewState(question: Question.WhatDateQuestion): WhatDateViewState {
        val dayEditable = question.dateComponents.contains(DatePart.DAY)
        val dayText = if (dayEditable) { "" } else { question.event.date.dayOfMonth.toString() }
        val monthEditable = question.dateComponents.contains(DatePart.MONTH)
        val monthText = if (monthEditable) { "" } else { question.event.date.monthValue.toString() }
        val yearEditable = question.dateComponents.contains(DatePart.YEAR)
        val yearText = if (yearEditable) { "" } else { question.event.date.year.toString() }

        return WhatDateViewState(
            showError = false,
            dayEditable = dayEditable,
            day = dayText,
            monthEditable = monthEditable,
            month = monthText,
            yearEditable = yearEditable,
            year = yearText
        )
    }
}

class QuizViewModel(
    private val service: RetrofitService
) : ViewModel() {
    private lateinit var question: Question
    val viewState = MutableLiveData<QuizViewState>()
    val showAnswerAlert = SingleLiveEvent<String>()
    val showQuizCompleteAlert = SingleLiveEvent<String>()
    val hideKeyboard = SingleLiveAction()
    val focusOnSubmit = SingleLiveAction()
    val focusOnDateInput = SingleLiveAction()
    val initializeDateInput = SingleLiveEvent<WhatDateViewState>()
    private lateinit var quiz: Quiz
    private val viewStateFactory = QuizViewStateFactory()

    fun initialize(categoryId: String, level: Int) {
        viewModelScope.launch {
            val category = service.category(categoryId)
            quiz = Quiz(category, level)
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        question = quiz.nextQuestion()
        val viewState = viewStateFactory.viewState(question, quiz.percentComplete)
        this.viewState.postValue(viewState)
        initializeDateInput.postValue(viewState.whatDateContent)
        if (question is Question.WhatDateQuestion) {
            focusOnDateInput.post()
        }
    }

    fun onSubmitClicked(whatDateAnswer: LocalDate?, whichEventAnswer: String?) {
        hideKeyboard.post()
        val answer = when (question) {
            is Question.WhatDateQuestion -> whatDateAnswer
            is Question.WhichEventQuestion -> whichEventAnswer
        }
        viewModelScope.launch {
            if (quiz.submitAnswer(answer, this@QuizViewModel::submitScore)) {
                showAnswerAlert.postValue("Correct!")
            } else {
                showAnswerAlert.postValue("Incorrect. The answer was ${question.correctAnswer}")
            }
        }
    }

    fun onDateChanged(date: LocalDate?) {
        if (question is Question.WhatDateQuestion) {
            val valid = date != null
            with(viewState.value) {
                this?.let {
                    val showError = if (valid) { false } else whatDateContent.showError
                    val newState = copy(
                        submitEnabled = valid,
                        whatDateContent = whatDateContent.copy(showError = showError)
                    )
                    viewState.postValue(newState)
                }
            }
        }
    }

    fun onOptionSelected(selectedEventId: String) {
        if (question is Question.WhichEventQuestion) {
            with(viewState.value) {
                this?.let {
                    val newState = copy(submitEnabled = true)
                    viewState.postValue(newState)
                }
            }
        }
    }

    fun onDateEntered(date: LocalDate?) {
        hideKeyboard.post()
        val valid = date != null
        with (viewState.value) {
            this?.let {
                val newState = copy(
                    whatDateContent = whatDateContent.copy(showError = !valid)
                )
                viewState.postValue(newState)
            }
            if (valid) {
                focusOnSubmit.post()
            }
        }
    }

    fun onAnswerDialogDismissed() {
        if (quiz.isComplete) {
            showQuizCompleteAlert.postValue("Quiz complete. You scored ${quiz.percentageScore}%")
        } else {
            nextQuestion()
        }
    }

    private fun submitScore(score: Score) {
        viewModelScope.launch {
            service.ratingsScore(score)
        }
    }
}
