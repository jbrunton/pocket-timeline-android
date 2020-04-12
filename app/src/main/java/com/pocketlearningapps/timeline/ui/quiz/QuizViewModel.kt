package com.pocketlearningapps.timeline.ui.quiz

import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.lib.SingleLiveAction
import com.pocketlearningapps.timeline.lib.SingleLiveEvent
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.coroutines.launch
import kotlin.random.Random

data class WhatDateViewState(
    val eventTitle: String
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
    val whichEventContent: WhichEventViewState
)

class QuizViewStateFactory {
    fun viewState(question: Question): QuizViewState {
        return when (question) {
            is Question.WhatDateQuestion -> QuizViewState(
                question = "On what date did the following event occur?",
                timelineTitle = question.timeline.title,
                showWhatDateContent = true,
                whatDateContent = WhatDateViewState(eventTitle = question.event.title),
                showWhichEventContent = false,
                whichEventContent = WhichEventViewState(emptyList())
            )
        }
    }
}

class QuizViewModel(private val service: RetrofitService) : ViewModel() {
    private lateinit var question: Question.WhatDateQuestion
    val viewState = MutableLiveData<QuizViewState>()
    val showAlert = MutableLiveData<String>()
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
            question = Question.WhatDateQuestion(timeline, event)
            viewState.postValue(viewStateFactory.viewState(question))
        }
    }

    fun onSubmitClicked(answer: String) {
        val correctAnswer = question.event.date.year.toString()
        if (answer == correctAnswer) {
            showAlert.postValue("Correct!")
        } else {
            showAlert.postValue("Incorrect. The answer was ${correctAnswer}")
        }
    }

    fun onDialogDismissed() {
        nextQuestion()
    }
}
