package com.pocketlearningapps.timeline.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pocketlearningapps.timeline.R
import com.pocketlearningapps.timeline.lib.requireInt
import com.pocketlearningapps.timeline.lib.requireString
import kotlinx.android.synthetic.main.dialog_continue.*
import java.io.Serializable

private const val ARG_THEME_ID = "ARG_THEME_ID"
private const val ARG_LABEL = "ARG_LABEL"

data class ContinueDialogState(
    val label: String,
    @StyleRes val theme: Int
) : Serializable

class ContinueDialog : BottomSheetDialogFragment() {
    init {
        isCancelable = false
    }

    interface Listener {
        fun onContinuePressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, arguments.requireInt(ARG_THEME_ID))
        return inflater.cloneInContext(contextThemeWrapper)
            .inflate(R.layout.dialog_continue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        continue_label.text = arguments.requireString(ARG_LABEL)
        continue_button.setOnClickListener {
            dismiss()
            (activity as Listener).onContinuePressed()
        }
    }

    companion object {
        fun build(state: ContinueDialogState): ContinueDialog {
            return ContinueDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_THEME_ID, state.theme)
                    putSerializable(ARG_LABEL, state.label)
                }
            }
        }
    }
}
