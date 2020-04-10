package com.pocketlearningapps.timeline.di

import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.main.MainViewModel

val UiModule = module {
    factory { MainViewModel(get(), get(), get()) }
}
