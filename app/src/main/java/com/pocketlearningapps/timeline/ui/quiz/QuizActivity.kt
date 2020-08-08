package com.pocketlearningapps.timeline.ui.quiz

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

class QuizActivity : AppCompatActivity(R.layout.activity_quiz), HasContainer, ContinueDialog.Listener {
    override val container by lazy { (application as HasContainer).container }
    private val viewModel: QuizViewModel by injectViewModel()
    private val keyboardHelper: KeyboardHelper by inject()
    private val categoryId by lazy { intent.getStringExtra("CATEGORY_ID") }
    private val level by lazy { intent.getIntExtra("LEVEL", 0) }

    override fun onContinuePressed() {
        viewModel.onContinuePressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        viewModel.viewState.distinctUntilChanged().observe(this, Observer { updateViewState(it) })
        viewModel.showQuizCompleteAlert.observe(this, Observer { showQuizCompletedAlert(it) })
        viewModel.hideKeyboard.observe(this, Observer { keyboardHelper.hideKeyboard(date_input) })
        viewModel.focusOnSubmit.observe(this, Observer { submit.requestFocus() })
        viewModel.initializeDateInput.observe(this, Observer { updateDateInput(it) })
        viewModel.focusOnDateInput.observe(this, Observer {
            keyboardHelper.showKeyboard(date_input_day)
            date_input_day.requestFocus()
        })
        viewModel.viewState.map { it.whichEventContent }
            .distinctUntilChanged()
            .observe(this, Observer { updateWhichEventViewState(it) })
        viewModel.showContinueDialog.observe(this, Observer {
            ContinueDialog.build(it).show(supportFragmentManager, "CONTINUE")
        })

        submit.setOnClickListener {
            viewModel.onSubmitClicked(date_input.date, which_event_options.selectedEventId)
        }
        date_input.onChanged = viewModel::onDateChanged
        date_input.onDoneAction = { viewModel.onDateEntered(date_input.date) }
        which_event_options.onOptionSelected = viewModel::onOptionSelected

        if (savedInstanceState == null) {
            viewModel.initialize(categoryId, level)
        }
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
        progress_bar.setProgress(viewState.percentComplete)
        question_title.text = viewState.questionTitle
        question_details.text = viewState.questionDetails
        category_description.text = viewState.categoryDescription

        what_date_content.isVisible = viewState.showWhatDateContent

        which_event_options.isVisible = viewState.showWhichEventContent

        submit.isEnabled = viewState.submitEnabled
    }

    private fun updateWhichEventViewState(viewState: WhichEventViewState) {
        which_event_options.updateView(viewState.options)
    }

    private fun updateDateInput(viewState: WhatDateViewState) {
        date_error.isVisible = viewState.showError
        date_input_day.isEnabled = viewState.dayEditable
        date_input.day = viewState.day
        date_input_month.isEnabled = viewState.monthEditable
        date_input.month = viewState.month
        date_input_year.isEnabled = viewState.yearEditable
        date_input.year = viewState.year

        if (viewState.dayEditable) {
            date_input_day.requestFocus()
        } else if (viewState.monthEditable) {
            date_input_month.requestFocus()
        } else if (viewState.yearEditable) {
            date_input_year.requestFocus()
        }
    }

    private fun showQuizCompletedAlert(message: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton("OK", { _, _ -> finishAfterTransition() })
            .show()
    }
}
