package com.pocketlearningapps.timeline.auth.session

class SessionManager(private val cookieJar: SessionCookieJar) {
    fun clear() {
        cookieJar.clearSession()
    }

    val hasSession get() = cookieJar.sessionCookie != null
}