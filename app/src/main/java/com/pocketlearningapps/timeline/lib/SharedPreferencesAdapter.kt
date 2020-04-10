package com.pocketlearningapps.timeline.lib

import android.content.Context
import com.google.gson.Gson
import java.io.Serializable

class SharedPreferencesAdapter(private val context: Context) {
    private val preferences by lazy {
        context.getSharedPreferences("session", Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    fun saveEntry(key: String, value: Serializable) {
        with (preferences.edit()) {
            putString(key, gson.toJson(value))
            commit()
        }
    }

    fun <T> readEntry(key: String, klass: Class<T>): T? {
        return preferences.getString(key, null)?.let {
            gson.fromJson(it, klass)
        }
    }
}
