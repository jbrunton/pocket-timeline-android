package com.pocketlearningapps.timeline.ui.quiz

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pocketlearningapps.timeline.R
import kotlinx.android.synthetic.main.dialog_continue.*

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
        val theme = arguments?.getInt("THEME") ?: throw java.lang.IllegalStateException("Missing theme")
        val contextThemeWrapper = ContextThemeWrapper(activity, theme)
        return activity?.layoutInflater?.cloneInContext(contextThemeWrapper)?.inflate(R.layout.dialog_continue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        continue_label.text = arguments?.getString("LABEL")
        continue_button.setOnClickListener {
            dismiss()
            (activity as Listener).onContinuePressed()
        }
    }

    companion object {
        fun build(state: ContinueWidgetState): ContinueDialog {
            return ContinueDialog().apply {
                arguments = Bundle().apply {
                    putSerializable("THEME", state.theme)
                    putSerializable("LABEL", state.label)
                }
            }
        }
    }
}
