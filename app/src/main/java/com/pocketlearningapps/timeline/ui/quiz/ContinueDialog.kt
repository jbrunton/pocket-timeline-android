package com.pocketlearningapps.timeline.ui.quiz

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pocketlearningapps.timeline.R

class ContinueDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.SubmitWidget_Incorrect_Theme)
        return activity?.layoutInflater?.cloneInContext(contextThemeWrapper)?.inflate(R.layout.dialog_continue, container, false)
    }
}
