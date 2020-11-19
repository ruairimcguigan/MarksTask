package com.demo.movies.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.demo.movies.TestMoviesApp
import java.io.IOException
import java.io.InputStreamReader

object ResponseReader {

    const val CHARSET = "UTF-8"

    fun readJson(fileName: String): String {
        try {
            val inputStream = (getInstrumentation()
                .targetContext
                .applicationContext as TestMoviesApp)
                .assets.open(fileName)

            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream,
                CHARSET
            )
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}