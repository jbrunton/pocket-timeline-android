package com.pocketlearningapps.timeline.ui.react

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import com.facebook.react.*
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.pocketlearningapps.timeline.BuildConfig

class MyReactActivity : ReactActivity(), DefaultHardwareBackBtnHandler {
    override fun getMainComponentName() = "MyReactNativeApp"

    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return object : ReactActivityDelegate(this, mainComponentName) {
            override fun getReactInstanceManager(): ReactInstanceManager {
                return ReactInstanceManager.builder()
                    .setApplication(application)
                    .setCurrentActivity(this@MyReactActivity)
                    .setBundleAssetName("index.android.bundle")
                    .addPackage(MainReactPackage())
                    .setUseDeveloperSupport(BuildConfig.DEBUG)
                    .setInitialLifecycleState(LifecycleState.RESUMED)
                    .build()
            }

            override fun getReactNativeHost(): ReactNativeHost {
                return object : ReactNativeHost(application) {
                    override fun getPackages(): MutableList<ReactPackage> = mutableListOf(
                        MainReactPackage()
                    )

                    override fun getUseDeveloperSupport() = BuildConfig.DEBUG

                    override fun getJSMainModuleName() = "index"
                }
            }
        }
    }
}
