package com.pocketlearningapps.timeline.lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.view_date_input.view.*
import java.lang.Integer.parseInt
import java.time.DateTimeException
import java.time.LocalDate


typealias DateInputChanged = (date: LocalDate?) -> Unit

private val MONTHS = arrayOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December")

class DateInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.view_date_input, this)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            android.R.layout.simple_dropdown_item_1line,
            MONTHS
        )
        val textView =
            findViewById<View>(R.id.date_input_month) as AutoCompleteTextView
        textView.setAdapter(adapter)

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
            if (MONTHS.contains(it?.toString())) {
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

        listOf<EditText>(date_input_day, date_input_month, date_input_year).forEach {
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
            val monthsText = date_input_month.text.toString()
            return if (MONTHS.contains(monthsText)) {
                MONTHS.indexOf(monthsText) + 1
            } else {
                null
            }
        }
        set(month) {
            date_input_month.setText(month?.let { MONTHS.get(it - 1) } ?: "")
        }

    var year: Int?
        get() {
            return try {
                parseInt(date_input_year.text.toString())
            } catch (e: NumberFormatException) {
                null
            }
        }
        set(year) {
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
