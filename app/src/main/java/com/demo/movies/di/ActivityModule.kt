package com.demo.movies.di

import com.demo.movies.ui.movies.AllMoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesMoviesActivity(): AllMoviesActivity
}