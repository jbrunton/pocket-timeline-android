package com.pocketlearningapps.timeline

import android.app.Application
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.pocketlearningapps.timeline.di.ApplicationComponent

class PocketTimelineApplication : Application(), HasContainer {
    override val container: Container = Container()

    override fun onCreate() {
        super.onCreate()
        container.register(ApplicationComponent(this))
    }
}
