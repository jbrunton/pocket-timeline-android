package com.pocketlearningapps.timeline.ui.quiz

import android.text.Editable
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.lib.SingleLiveAction
import com.pocketlearningapps.timeline.lib.SingleLiveEvent
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class WhatDateViewState(
    val eventTitle: String,
    val showError: Boolean
)

data class WhichEventViewState(
    val options: List<Event>
)

data class QuizViewState(
    val question: String,
    val timelineTitle: String,
    val showWhatDateContent: Boolean,
    val whatDateContent: WhatDateViewState,
    val showWhichEventContent: Boolean,
    val whichEventContent: WhichEventViewState,
    val submitEnabled: Boolean
)

class QuizViewStateFactory {
    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    fun viewState(question: Question): QuizViewState {
        return when (question) {
            is Question.WhatDateQuestion -> QuizViewState(
                question = "On what date did the following event occur?",
                timelineTitle = question.timeline.title,
                showWhatDateContent = true,
                whatDateContent = WhatDateViewState(eventTitle = question.event.title, showError = false),
                showWhichEventContent = false,
                whichEventContent = WhichEventViewState(emptyList()),
                submitEnabled = false
            )
            is Question.WhichEventQuestion -> QuizViewState(
                question = "Which of the following events occurred on\n${question.event.date.format(dateFormatter)}?",
                timelineTitle = question.timeline.title,
                showWhatDateContent = false,
                whatDateContent = WhatDateViewState("", showError = false),
                showWhichEventContent = true,
                whichEventContent = WhichEventViewState(question.options),
                submitEnabled = false
            )
        }
    }


}

class QuizViewModel(private val service: RetrofitService) : ViewModel() {
    private lateinit var question: Question
    val viewState = MutableLiveData<QuizViewState>()
    val showAlert = SingleLiveEvent<String>()
    val hideKeyboard = SingleLiveAction()
    val focusOnSubmit = SingleLiveAction()
    val focusOnDateInput = SingleLiveAction()
    val clearDateInput = SingleLiveAction()
    private lateinit var timelines: List<Timeline>
    private val viewStateFactory = QuizViewStateFactory()

    init {
        viewModelScope.launch {
            timelines = service.timelines()
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        val timeline = timelines.get(Random.nextInt(timelines.size))
        viewModelScope.launch {
            val timelineEvents = service.timeline(timeline.id).events
            val event = timelineEvents.get(Random.nextInt(timelineEvents.size))
            question = if (Random.nextBoolean()) {
                Question.WhatDateQuestion(timeline, event)
            } else {
                Question.WhichEventQuestion(timeline, event, pickEventOptions(timelineEvents, event))
            }
            viewState.postValue(viewStateFactory.viewState(question))
            clearDateInput.post()
            if (question is Question.WhatDateQuestion) {
                focusOnDateInput.post()
            }
        }
    }

    private fun pickEventOptions(events: List<Event>, correctAnswer: Event): List<Event> {
        return events
            .filter { it.id != correctAnswer.id }
            .sortedBy { Random.nextInt() }
            .take(2)
            .plus(correctAnswer)
    }

    fun onSubmitClicked(whatDateAnswer: LocalDate?, whichEventAnswer: String?) {
        val answer = when (question) {
            is Question.WhatDateQuestion -> whatDateAnswer
            is Question.WhichEventQuestion -> whichEventAnswer ?: ""
        }
        if (question.validate(answer)) {
            showAlert.postValue("Correct!")
        } else {
            showAlert.postValue("Incorrect. The answer was ${question.correctAnswer}")
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

    fun onDialogDismissed() {
        nextQuestion()
    }
}
