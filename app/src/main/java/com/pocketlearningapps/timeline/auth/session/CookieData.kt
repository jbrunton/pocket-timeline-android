package com.pocketlearningapps.timeline.auth.session

import okhttp3.Cookie
import java.io.Serializable

data class CookieData(
    val name: String,
    val value: String,
    val expiresAt: Long,
    val domain: String,
    val path: String,
    val secure: Boolean,
    val httpOnly: Boolean,
    val persistent: Boolean,
    val hostOnly: Boolean
) : Serializable

fun Cookie.toData() = CookieData(
    name = name,
    value = value,
    expiresAt = expiresAt,
    domain = domain,
    path = path,
    secure = secure,
    httpOnly = httpOnly,
    persistent = persistent,
    hostOnly = hostOnly
)

fun CookieData.toCookie(): Cookie {
    val builder = Cookie.Builder()
        .name(name)
        .value(value)
        .domain(domain)
        .path(path)

    if (secure) builder.secure()
    if (httpOnly) builder.httpOnly()
    if (hostOnly) builder.hostOnlyDomain(domain)
    if (persistent) builder.expiresAt(expiresAt)

    return builder.build()
}
