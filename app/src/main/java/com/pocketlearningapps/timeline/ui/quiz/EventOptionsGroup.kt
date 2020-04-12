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

    fun updateView(viewState: List<Event>) {
        if (viewState.size != childCount) {
            removeAllViews()
            viewState.forEach { addOption(it) }
        }
    }

    private fun addOption(event: Event) {
        val view = RadioButton(context)
        view.text = event.title
        view.tag = event.id
        view.setOnCheckedChangeListener { _, isChecked -> selectedEventId = event.id }
        addView(view)
    }

//    inner class EventView(context: Context) : LinearLayout(context) {
//        init {
//            inflate(context, R.layout.dirty_car_categories_view, this)
//        }
//
//        fun updateView(viewState: CategoryViewState) {
//            categoryDescription.text = viewState.category.name
//            categoryCheckbox.isChecked = viewState.isChecked
//            categoryCheckbox.setOnCheckedChangeListener { _, isChecked ->
//                listener.onCheckboxChanged(viewState.category.id, isChecked)
//            }
//        }
//    }
}
