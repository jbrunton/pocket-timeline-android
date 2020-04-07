package com.pocketlearningapps.timeline.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter

class MainViewModelFactory(
    private val signInAdapter: GoogleSignInAdapter
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(signInAdapter) as T
    }
}