package com.pocketlearningapps.timeline.lib

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import java.io.Serializable

class SharedPreferencesAdapter(private val context: Context) {
    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val gson = Gson()

    fun saveEntry(key: String, value: Serializable) {
        with (sharedPreferences.edit()) {
            putString(key, gson.toJson(value))
            commit()
        }
    }

    fun <T> readEntry(key: String, klass: Class<T>): T? {
        return sharedPreferences.getString(key, null)?.let {
            gson.fromJson(it, klass)
        }
    }

    fun getString(key: String) = sharedPreferences.getString(key, null)

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)
}
