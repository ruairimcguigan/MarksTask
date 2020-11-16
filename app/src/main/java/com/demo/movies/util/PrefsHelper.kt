package com.demo.movies.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class PrefsHelper @Inject constructor() {

    fun read(context: Context):Boolean = context
        .getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        .getBoolean(HAS_SUBMITTED_CREDENTIALS, false)

    fun write(context: Context, hasSubmitted: Boolean) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(HAS_SUBMITTED_CREDENTIALS, hasSubmitted)
            commit()
        }
    }

    companion object {
        const val PREF_NAME = "prefs"
        const val HAS_SUBMITTED_CREDENTIALS = "credentialsSubmitted"
    }
}