package com.demo.movies.util

import android.app.Application
import android.preference.PreferenceManager.getDefaultSharedPreferences
import javax.inject.Inject

class PrefsHelper @Inject constructor(val application: Application) {

    fun read(): String?
            = getDefaultSharedPreferences(application).getString(IMAGE_PATH, null)

    fun write(imagePath: String) {
        val sharedPref = getDefaultSharedPreferences(application) ?: return
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