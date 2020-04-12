package com.pocketlearningapps.timeline.lib

import androidx.fragment.app.Fragment

fun Fragment.requireStringArgument(key: String): String {
    return requireArguments().getString(key)
        ?: throw IllegalStateException("Expected ${this} to have argument ${key}")
}
