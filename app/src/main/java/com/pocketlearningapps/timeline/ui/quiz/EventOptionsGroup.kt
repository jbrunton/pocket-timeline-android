package com.pocketlearningapps.timeline.ui.quiz

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.pocketlearningapps.timeline.entities.Event

class EventOptionsGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    init {
        orientation = VERTICAL
    }

    var selectedEventId: String? = null
    var onOptionSelected: ((String) -> Unit)? = null

    fun updateView(viewState: List<Event>) {
        selectedEventId = null
        removeAllViews()
        viewState.forEach { addOption(it) }
    }

    private fun addOption(event: Event) {
        val view = RadioButton(context)
        view.text = event.title
        view.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedEventId = event.id
                onOptionSelected?.invoke(event.id)
            }
        }
        addView(view)
    }
}
