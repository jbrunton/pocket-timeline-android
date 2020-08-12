package com.pocketlearningapps.timeline.ui.react

import android.os.Bundle
import com.facebook.react.*
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.pocketlearningapps.timeline.BuildConfig


class MyReactActivity : ReactActivity(), DefaultHardwareBackBtnHandler {
    override fun getMainComponentName() = "ReactTest"

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

            override fun getLaunchOptions(): Bundle? {
                val options = Bundle()
                options.putString("timeline_id", intent.getStringExtra("timeline_id"))
                options.putString("timeline_title", intent.getStringExtra("timeline_title"))
                return options
            }

            override fun getReactNativeHost(): ReactNativeHost {
                return object : ReactNativeHost(application) {
                    override fun getPackages() = PackageList(this).packages

                    override fun getUseDeveloperSupport() = BuildConfig.DEBUG

                    override fun getJSMainModuleName() = "index"
                }
            }
        }
    }
}
