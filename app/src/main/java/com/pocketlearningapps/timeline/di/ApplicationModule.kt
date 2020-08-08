package com.pocketlearningapps.timeline.di

import android.app.Application
import android.content.Context
import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.lib.KeyboardHelper
import com.pocketlearningapps.timeline.lib.SharedPreferencesAdapter

fun ApplicationModule(application: Application) = module {
    single<Context> { application }
    single { SharedPreferencesAdapter(get()) }
    single { KeyboardHelper(get()) }
}
