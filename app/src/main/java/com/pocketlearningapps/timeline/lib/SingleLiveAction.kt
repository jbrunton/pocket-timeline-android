package com.pocketlearningapps.timeline.lib

import androidx.annotation.MainThread

class SingleLiveAction : SingleLiveEvent<Unit>() {
    @MainThread
    fun call() {
        super.setValue(Unit)
    }

    fun post() {
        super.postValue(Unit)
    }
}
