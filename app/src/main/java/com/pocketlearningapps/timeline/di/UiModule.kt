package com.pocketlearningapps.timeline.di

import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.ui.account.AccountViewModel

val UiModule = module {
    factory {
        AccountViewModel(
            get(),
            get(),
            get()
        )
    }
}
