package com.demo.movies.util

import android.app.Application
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class UtilModule {
    @Provides
    fun providePrefsHelper(application: Application) = PrefsHelper(application)

    @Provides
    fun providePathBuilder(
        application: Application,
        prefsHelper: PrefsHelper
    ) = ImgPathBuilder(
        application,
        prefsHelper
    )

    @Provides
    fun provideScheduler(): SchedulerProvider = DefaultSchedulerProvider()
}