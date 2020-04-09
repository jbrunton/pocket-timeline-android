package com.pocketlearningapps.timeline.di

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jbrunton.inject.module
import com.pocketlearningapps.timeline.auth.GoogleSignInAdapter
import com.pocketlearningapps.timeline.network.RetrofitServiceFactory
import okhttp3.CookieJar

val HttpModule = module {
    single<CookieJar> {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(get<Context>()))
    }

    single { RetrofitServiceFactory(get(), get()).create() }

    // TODO: possibly move this to an AuthModule
    single { GoogleSignInAdapter(get()) }
}
