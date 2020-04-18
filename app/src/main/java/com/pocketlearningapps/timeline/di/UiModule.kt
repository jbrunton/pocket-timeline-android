package com.pocketlearningapps.timeline.di

import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.ui.account.AccountViewModel
import com.pocketlearningapps.timeline.ui.quiz.QuizViewModel

val UiModule = module {
    factory { AccountViewModel(get(), get(), get()) }
    factory { QuizViewModel(get(), get()) }
}
