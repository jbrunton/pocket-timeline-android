package com.pocketlearningapps.timeline.di

import android.app.Application
import com.jbrunton.inject.Container
import com.jbrunton.inject.Module

class ApplicationComponent(val application: Application) : Module {
    override fun registerTypes(container: Container) {
        container.register(ApplicationModule(application), HttpModule, UiModule)
    }
}
