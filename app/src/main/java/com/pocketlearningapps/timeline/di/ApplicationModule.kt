package com.pocketlearningapps.timeline.di

import android.app.Application
import android.content.Context
import com.jbrunton.inject.module

fun ApplicationModule(application: Application) = module {
    single<Context> { application }
}
