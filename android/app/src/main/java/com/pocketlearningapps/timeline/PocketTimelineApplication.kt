package com.pocketlearningapps.timeline

import android.app.Application
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.pocketlearningapps.timeline.di.ApplicationComponent

class PocketTimelineApplication : Application(), HasContainer, ReactApplication {
    override val container: Container = Container()

    override fun onCreate() {
        super.onCreate()
        container.register(ApplicationComponent(this))
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return object : ReactNativeHost(this) {
            override fun getPackages(): MutableList<ReactPackage> = mutableListOf(
                MainReactPackage()
            )

            override fun getUseDeveloperSupport() = BuildConfig.DEBUG
        }
    }
}
