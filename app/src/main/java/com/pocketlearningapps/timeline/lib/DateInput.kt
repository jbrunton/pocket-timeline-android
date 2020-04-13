package com.pocketlearningapps.timeline.lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.fragment_quiz_selector.view.*
import kotlinx.android.synthetic.main.view_date_input.view.*
import java.lang.Integer.parseInt
import java.lang.NumberFormatException
import java.time.DateTimeException
import java.time.LocalDate

typealias DateInputChanged = (date: LocalDate?) -> Unit

class DateInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.view_date_input, this)

        date_input_day.doAfterTextChanged {
            if (it?.length ?: 0 >= 2) {
                Log.d("DateInput", "date_input_day.doAfterTextChanged")
                date_input_month.requestFocus()
            }
        }
        date_input_day.setOnEditorActionListener { v, actionId, event ->
            Log.d("DateInput", "date_input_day.setOnEditorActionListener")
            date_input_month.requestFocus()
            true
        }
        date_input_month.doAfterTextChanged {
            if (it?.length ?: 0 >= 2) {
                Log.d("DateInput", "date_input_month.doAfterTextChanged")
                date_input_year.requestFocus()
            }
        }
        date_input_month.setOnEditorActionListener { v, actionId, event ->
            Log.d("DateInput", "date_input_month.setOnEditorActionListener")
            date_input_year.requestFocus()
            true
        }
        date_input_year.setOnEditorActionListener { v, actionId, event ->
            Log.d("DateInput", "date_input_year.setOnEditorActionListener")
            onDoneAction?.invoke()
            true
        }

        listOf(date_input_day, date_input_month, date_input_year).forEach {
            it.doAfterTextChanged {
                onChanged?.invoke(date)
            }
        }
    }

    var onChanged: DateInputChanged? = null
    var onDoneAction: (() -> Unit)? = null

    var day: Int?
        get() {
            return try {
                parseInt(date_input_day.text.toString())
            } catch (e: NumberFormatException) {
                null
            }
        }
        set(day) {
            date_input_day.setText(day?.let(Int::toString) ?: "")
        }

    var month: Int?
        get() {
            return try {
                parseInt(date_input_month.text.toString())
            } catch (e: NumberFormatException) {
                null
            }
        }
        set(month) {
            date_input_month.setText(month?.let(Int::toString) ?: "")
        }

    var year: Int?
        get() {
            return try {
                parseInt(date_input_year.text.toString())
            } catch (e: NumberFormatException) {
                null
            }
        }
        set(year: Int?) {
            date_input_year.setText(year?.let(Int::toString) ?: "")
        }

    var date: LocalDate?
        get() {
            val (d, m, y) = Triple(day, month, year)
            return if (d == null || m == null || y == null) {
                null
            } else {
                try {
                    LocalDate.of(y, m, d)
                } catch (e: DateTimeException) {
                    null
                }
            }
        }
        set(date) {
            if (date == null) {
                day = null
                month = null
                year = null
            } else {
                day = date.dayOfMonth
                month = date.monthValue
                year = date.year
            }
        }
}
