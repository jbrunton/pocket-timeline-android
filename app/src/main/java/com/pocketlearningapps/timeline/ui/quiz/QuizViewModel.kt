package com.pocketlearningapps.timeline.ui.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val showError: Boolean
)

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
                whatDateContent = WhatDateViewState(showError = false),
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
                whatDateContent = WhatDateViewState(showError = false),
                showWhichEventContent = true,
                whichEventContent = WhichEventViewState(question.options, UUID.randomUUID()),
                submitEnabled = false,
                percentComplete = percentComplete,
                categoryDescription = question.category.description
            )
        }
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
    val clearDateInput = SingleLiveAction()
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
        viewState.postValue(viewStateFactory.viewState(question, quiz.percentComplete))
        clearDateInput.post()
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
