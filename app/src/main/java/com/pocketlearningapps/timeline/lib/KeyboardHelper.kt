package com.pocketlearningapps.timeline.lib

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardHelper(private val context: Context) {
    private val inputMethodManager by lazy {
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun hideKeyboard(view: View) {
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
