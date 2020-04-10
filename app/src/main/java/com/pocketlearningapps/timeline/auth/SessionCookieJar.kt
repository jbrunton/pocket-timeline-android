package com.pocketlearningapps.timeline.auth

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

private const val SESSION_COOKIE_NAME = "_pocket_timeline_session"

class SessionCookieJar : CookieJar {
    private var sessionCookie: Cookie? = null

    @Synchronized override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return listOf(sessionCookie).filterNotNull()
    }

    @Synchronized override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.find { it.name == SESSION_COOKIE_NAME }?.let {
            sessionCookie = it
        }
    }

    fun clearSession() {
        sessionCookie = null
    }

    val hasSession get() = sessionCookie != null
}
