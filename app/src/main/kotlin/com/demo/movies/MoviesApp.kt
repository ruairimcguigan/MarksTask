package com.demo.movies

import com.demo.movies.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber.DebugTree
import timber.log.Timber.plant

open class MoviesApp : DaggerApplication(){

    override fun onCreate() {
        super.onCreate()
        plant(DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent
            .builder()
            .application(this)
            .build()

    open fun getBaseUrl() = BuildConfig.BASE_URL
}