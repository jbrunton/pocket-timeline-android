package com.pocketlearningapps.timeline.di

import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.auth.session.SessionCookieJar
import com.pocketlearningapps.timeline.auth.session.SessionManager
import com.pocketlearningapps.timeline.network.RetrofitServiceFactory

val HttpModule = module {
    single { SessionCookieJar(get()) }

    single { RetrofitServiceFactory(get()).create() }

    // TODO: possibly move this to an AuthModule
    single { GoogleSignInAdapter(get(), get()) }
    single { SessionManager(get()) }
}
