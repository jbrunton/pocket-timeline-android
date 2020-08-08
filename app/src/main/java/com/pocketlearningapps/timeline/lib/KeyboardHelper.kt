package com.pocketlearningapps.timeline.lib

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardHelper(private val context: Context) {
    private val inputMethodManager by lazy {
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun hideKeyboard(view: View) {
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        Handler().postDelayed({
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }
}
