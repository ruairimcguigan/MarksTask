package com.demo.movies.testrunner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.demo.movies.TestMoviesApp

class MockTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?): Application =
        super.newApplication(
        classLoader,
        TestMoviesApp::class.java.name,
        context
    )
}