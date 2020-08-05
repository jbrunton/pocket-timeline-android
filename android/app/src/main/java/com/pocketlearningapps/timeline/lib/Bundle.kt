package com.pocketlearningapps.timeline.lib

import android.os.Bundle
import java.io.Serializable

fun Bundle?.requireInt(key: String): Int =
    this?.getInt(key) ?: throw IllegalStateException("Missing argument $key")

fun Bundle?.requireString(key: String): String =
    this?.getString(key) ?: throw IllegalStateException("Missing argument $key")

fun <T : Serializable> Bundle?.requireSerializable(key: String): T =
    this?.getSerializable(key) as? T ?: throw IllegalStateException("Missing argument $key")
