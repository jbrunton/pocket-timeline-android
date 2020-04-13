package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jbrunton.inject.*
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.entities.Timeline
import com.pocketlearningapps.timeline.lib.KeyboardHelper
import com.pocketlearningapps.timeline.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.view_date_input.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.random.Random

class QuizFragment : Fragment(R.layout.fragment_quiz), HasContainer {
    override val container by lazy { (activity?.application as HasContainer).container }
    private val viewModel: QuizViewModel by injectViewModel()
    private val keyboardHelper: KeyboardHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { updateViewState(it) })
        viewModel.showAlert.observe(viewLifecycleOwner, Observer { showAlert(it) })
        viewModel.hideKeyboard.observe(viewLifecycleOwner, Observer { keyboardHelper.hideKeyboard(date_input) })
        viewModel.focusOnSubmit.observe(viewLifecycleOwner, Observer { submit.requestFocus() })
        viewModel.clearDateInput.observe(viewLifecycleOwner, Observer { date_input.date = null })
        viewModel.focusOnDateInput.observe(viewLifecycleOwner, Observer {
            keyboardHelper.showKeyboard(date_input_day)
            date_input_day.requestFocus()
        })

        submit.setOnClickListener {
            viewModel.onSubmitClicked(date_input.date, which_event_options.selectedEventId)
        }
        date_input.onChanged = viewModel::onDateChanged
        date_input.onDoneAction = { viewModel.onDateEntered(date_input.date) }
    }

    private fun updateViewState(viewState: QuizViewState) {
        //date_input.date = null
        question.text = viewState.question
        timeline_title.text = viewState.timelineTitle

        what_date_content.isVisible = viewState.showWhatDateContent
        event_title.text = viewState.whatDateContent.eventTitle
        date_error.isVisible = viewState.whatDateContent.showError

        which_event_options.isVisible = viewState.showWhichEventContent
        which_event_options.updateView(viewState.whichEventContent.options)

        submit.isEnabled = viewState.submitEnabled
    }

    private fun showAlert(message: String) {
        MaterialAlertDialogBuilder(activity)
            .setMessage(message)
            .setPositiveButton("OK", { _, _ -> viewModel.onDialogDismissed() })
            .show()
    }
}

