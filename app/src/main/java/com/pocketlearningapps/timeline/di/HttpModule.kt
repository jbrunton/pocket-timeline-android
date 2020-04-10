package com.pocketlearningapps.timeline.di

import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.auth.SessionCookieJar
import com.pocketlearningapps.timeline.network.RetrofitServiceFactory

val HttpModule = module {
    single { SessionCookieJar() }

    single { RetrofitServiceFactory(get()).create() }

    // TODO: possibly move this to an AuthModule
    single { GoogleSignInAdapter(get(), get()) }
}
