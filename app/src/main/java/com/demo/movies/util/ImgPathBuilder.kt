package com.demo.movies.util

import android.app.Application
import javax.inject.Inject

class ImgPathBuilder@Inject constructor(
    val application: Application,
    private val prefsHelper: PrefsHelper
) {
    fun buildPosterPath(path: String): String {
        val imageUrl = prefsHelper.read()
        return StringBuilder()
            .append(imageUrl)
            .append(path)
            .toString()
    }
}