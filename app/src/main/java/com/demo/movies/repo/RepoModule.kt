package com.demo.movies.repo

import com.demo.movies.api.MoviesService
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import dagger.Module
import dagger.Provides

@Module
class RepoModule {

    @Provides fun provideRxScheduler() = DefaultSchedulerProvider()

    @Provides fun provideDisposable() = RxDisposable()

    @Provides
    fun providesRepository(
        disposable: RxDisposable,
        defaultSchedulerProvider: DefaultSchedulerProvider,
        service: MoviesService
    ): Repository = DefaultRepository(
        service = service,
        disposable = disposable,
        schedulerProvider = defaultSchedulerProvider
    )
}