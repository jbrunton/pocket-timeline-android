package com.pocketlearningapps.timeline.lib

import android.os.Bundle

fun Bundle?.requireInt(key: String): Int = this?.getInt(key)
    ?: throw IllegalStateException("Missing argument $key")

fun Bundle?.requireString(key: String): String = this?.getString(key)
    ?: throw IllegalStateException("Missing argument $key")
