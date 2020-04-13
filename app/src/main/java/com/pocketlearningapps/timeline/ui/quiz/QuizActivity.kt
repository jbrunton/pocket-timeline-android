package com.pocketlearningapps.timeline.ui.quiz

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jbrunton.inject.*
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.lib.KeyboardHelper
import com.snakydesign.livedataextensions.distinctUntilChanged
import com.snakydesign.livedataextensions.map
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.view_date_input.*

class QuizActivity : AppCompatActivity(R.layout.activity_quiz), HasContainer {
    override val container by lazy { (application as HasContainer).container }
    private val viewModel: QuizViewModel by injectViewModel()
    private val keyboardHelper: KeyboardHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
        progress_bar.setProgress(10)

        viewModel.viewState.observe(this, Observer { updateViewState(it) })
        viewModel.showAlert.observe(this, Observer { showAlert(it) })
        viewModel.hideKeyboard.observe(this, Observer { keyboardHelper.hideKeyboard(date_input) })
        viewModel.focusOnSubmit.observe(this, Observer { submit.requestFocus() })
        viewModel.clearDateInput.observe(this, Observer { date_input.date = null })
        viewModel.focusOnDateInput.observe(this, Observer {
            keyboardHelper.showKeyboard(date_input_day)
            date_input_day.requestFocus()
        })
        viewModel.viewState.map { it.whichEventContent }
            .distinctUntilChanged()
            .observe(this, Observer { updateWhichEventViewState(it) })

        submit.setOnClickListener {
            viewModel.onSubmitClicked(date_input.date, which_event_options.selectedEventId)
        }
        date_input.onChanged = viewModel::onDateChanged
        date_input.onDoneAction = { viewModel.onDateEntered(date_input.date) }
        which_event_options.onOptionSelected = viewModel::onOptionSelected
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED)
            finishAfterTransition()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateViewState(viewState: QuizViewState) {
        //date_input.date = null
        question_title.text = viewState.questionTitle
        question_details.text = viewState.questionDetails

        what_date_content.isVisible = viewState.showWhatDateContent
        date_error.isVisible = viewState.whatDateContent.showError

        which_event_options.isVisible = viewState.showWhichEventContent

        submit.isEnabled = viewState.submitEnabled
    }

    private fun updateWhichEventViewState(viewState: WhichEventViewState) {
        which_event_options.updateView(viewState.options)
    }

    private fun showAlert(message: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton("OK", { _, _ -> viewModel.onDialogDismissed() })
            .show()
    }
}
