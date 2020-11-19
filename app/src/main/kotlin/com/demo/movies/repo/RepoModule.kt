package com.demo.movies.repo

import android.app.Application
import com.demo.movies.api.MoviesService
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import com.demo.movies.util.PrefsHelper
import dagger.Module
import dagger.Provides

@Module
class RepoModule {

    @Provides fun provideRxScheduler() = DefaultSchedulerProvider()

    @Provides fun provideDisposable() = RxDisposable()

    @Provides
    fun providesRepository(
        application: Application,
        disposable: RxDisposable,
        defaultSchedulerProvider: DefaultSchedulerProvider,
        service: MoviesService,
        prefsHelper: PrefsHelper
    ): Repository = DefaultRepository(
        application = application,
        service = service,
        disposable = disposable,
        schedulerProvider = defaultSchedulerProvider,
        prefsHelper = prefsHelper
    )
}