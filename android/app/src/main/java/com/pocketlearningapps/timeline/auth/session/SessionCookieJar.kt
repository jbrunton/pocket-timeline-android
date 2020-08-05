package com.pocketlearningapps.timeline.auth.session

import com.pocketlearningapps.timeline.lib.SharedPreferencesAdapter
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

private const val SESSION_COOKIE_NAME = "_pocket_timeline_session"

class SessionCookieJar(
    private val preferences: SharedPreferencesAdapter
) : CookieJar {
    var sessionCookie: Cookie?
        private set

    init {
        sessionCookie = preferences.readEntry(
            SESSION_COOKIE_NAME,
            CookieData::class.java
        )?.toCookie()
    }

    @Synchronized override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return listOfNotNull(sessionCookie)
    }

    @Synchronized override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.find { it.name == SESSION_COOKIE_NAME }?.let { cookie ->
            sessionCookie = cookie
            preferences.saveEntry(SESSION_COOKIE_NAME, cookie.toData())
        }
    }

    fun clearSession() {
        sessionCookie = null
    }
}
