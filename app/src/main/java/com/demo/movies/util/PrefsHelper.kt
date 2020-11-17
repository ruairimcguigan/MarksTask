package com.demo.movies.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class PrefsHelper @Inject constructor() {

    fun read(context: Context): String? = context
        .getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        .getString(IMAGE_PATH, null)

    fun write(context: Context, imagePath: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(IMAGE_PATH, imagePath)
            commit()
        }
    }

    companion object {
        const val PREF_NAME = "prefs"
        const val IMAGE_PATH = "imageUrl"
    }
}